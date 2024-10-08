package com.example.complaint_tracker.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.complaint_tracker.DTO.AdminLogin;
import com.example.complaint_tracker.DTO.Login;
import com.example.complaint_tracker.model.ComplaintDetails;
import com.example.complaint_tracker.model.StaffDetails;
import com.example.complaint_tracker.model.StudentDetails;
import com.example.complaint_tracker.repository.ComplaintsRepo;
import com.example.complaint_tracker.repository.StaffDetailsRepo;
import com.example.complaint_tracker.repository.StudentDetailsRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class StudentController {

    @Autowired
    private StudentDetailsRepo studentRepo;

    @Autowired
    private StaffDetailsRepo staffRepo;

    @Autowired
    private ComplaintsRepo complaintsRepo;

    // Directory to save uploaded files
    //private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/usertype")
    public String usertype() {
        return "userConfirmation";
    }

    @GetMapping("/studentlogin")
    public String studentLogin() {
        return "studentLogin";
    }
    
    @GetMapping("/studentdashboard")
    public String studentdashboard() {
        return "studentdashboard";
    }

    @GetMapping("/stafflogin")
    public String staffLogin() {
        return "staffLogin";
    }
    @GetMapping("/staffdashboard")
    public String staffdashboard() {
        return "staffDashboard";
    }

    @GetMapping("/filecomplaint")
    public String fileComplaint() {
        return "complaintForm";
    }

    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "adminLogin";
    }
    @GetMapping("/adminDashboard")
    public String adminDashboard() {
        return "adminDashboard";
    }

    @GetMapping("/studentregister")
    public String studentRegister() {
        return "studentRegister";
    }

    @GetMapping("/staffregister")
    public String staffRegister() {
        return "staffRegister";
    }

    // Student Registration
    @PostMapping("/studentregistration")
    public String studentRegister(@Valid @ModelAttribute StudentDetails student, BindingResult result, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (result.hasErrors()) {
            return "studentRegister";
        }

        if (studentRepo.findByEmail(student.getEmail()) != null) {
            model.addAttribute("status", "Email already exists!");
            return "studentRegister";
        }

        String encodedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);
        student.setUsertype("student");

        studentRepo.save(student);

        return "redirect:studentlogin";
    }

    // Staff Registration
    @PostMapping("/staffregistration")
    public String staffRegister(@Valid @ModelAttribute StaffDetails staff, BindingResult result, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (result.hasErrors()) {
            return "staffRegister";
        }

        if (staffRepo.findByEmail(staff.getEmail()) != null) {
            model.addAttribute("status", "Email already exists!");
            return "staffRegister";
        }

        String encodedPassword = passwordEncoder.encode(staff.getPassword());
        staff.setPassword(encodedPassword);
        staff.setUsertype("staff");

        staffRepo.save(staff);

        return "redirect:stafflogin";
    }

    // Student Login
    @PostMapping("/loginstudent")
    public String loginStudent(@ModelAttribute Login studentDetails, Model model, HttpSession session) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rollNo = studentDetails.getId();
        String password = studentDetails.getPassword();

        StudentDetails existingStudent = studentRepo.findByRollNo(rollNo);

        if (existingStudent == null) {
            model.addAttribute("status", "User does not exist!");
            return "studentLogin";
        }

        if (!passwordEncoder.matches(password, existingStudent.getPassword())) {
            model.addAttribute("status", "Incorrect password");
            return "studentLogin";
        }

        session.setAttribute("studentRollNo", existingStudent.getRollNo());
        session.setAttribute("type", "student");
        return "studentdashboard";
    }

    // Staff Login
    @PostMapping("/loginstaff")
    public String loginStaff(@ModelAttribute Login studentDetails, Model model, HttpSession session) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String id = studentDetails.getId();
        String password = studentDetails.getPassword();

        StaffDetails existingStaff = staffRepo.findByStaffid(id);

        if (existingStaff == null) {
            model.addAttribute("status", "User does not exist!");
            return "staffLogin";
        }

        if (!passwordEncoder.matches(password, existingStaff.getPassword())) {
            model.addAttribute("status", "Incorrect password");
            return "staffLogin";
        }

        session.setAttribute("staffId", existingStaff.getId());
        session.setAttribute("type", "staff");
        return "staffdashboard";
    }

    // Admin Login
    @PostMapping("/loginadmin")
    public String loginAdmin(@ModelAttribute AdminLogin admin, Model model) {
        String username = admin.getUsername();
        String password = admin.getPassword();

        if (!"admin".equals(username) || !"nimda".equals(password)) {
            model.addAttribute("status", "Invalid credentials");
            return "adminLogin";
        }

        return "adminDashboard";
    }
    
    
@PostMapping("/submitcomplaint")
public String submitComplaint(@ModelAttribute ComplaintDetails complaint,

                                                 @RequestParam("evidence") MultipartFile evidenceFile,Model model, HttpSession session) {
        try {            
            // Handle file upload (if applicable)

            if (evidenceFile != null && !evidenceFile.isEmpty()) {
                // Generate a unique filename to avoid conflicts
            	System.out.println(complaint.getDesiredResolution());
                String fileName = UUID.randomUUID().toString() + "." + evidenceFile.getOriginalFilename().split("\\.")[1];
                // Define the upload directory (replace with your desired location)
                System.out.println(fileName);
                String uploadDir = "./uploads";
                // Create the upload directory if it doesn't exist
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {

                    try {

                        Files.createDirectories(uploadPath);

                    } catch (IOException e) {

                        e.printStackTrace();   
                        // Return dedicated error view (replace "error" with your view name)
                        return "error";
                    }
                }

                // Save the uploaded file to the designated location
                try {

                    Files.write(Paths.get(uploadDir, fileName), evidenceFile.getBytes());

                    complaint.setEvidenceFileName(fileName); // Update complaint entity

                } catch (IOException e) {

                    e.printStackTrace();

                    // Return dedicated error view (replace "error" with your view name)
                    model.addAttribute("status","Error");
                    return "complaintForm";
                }
            }
            String userType=(String) session.getAttribute("type");
            complaint.setGivenBy(userType);
            complaint.setStatus("submitted");
            complaintsRepo.save(complaint);
            // Return the view name (replace "complaintSubmitted" with your actual view name)
            model.addAttribute("status","Complaint submitted successfully");
            
            return "complaintForm";
        } catch (Exception e) {

            e.printStackTrace();
            // In case of errors, consider redirecting to a generic error page with details
            model.addAttribute("status",e);
            return "redirect:/filecomplaint";

        }

    }

@GetMapping("/complaints")
public String displayComplaints(Model model) {
    List<ComplaintDetails> complaints = complaintsRepo.findAll();
    model.addAttribute("complaints", complaints);
    return "complaints";
}

@GetMapping("/admincomplaints")
public String displayAdminComplaints(Model model) {
    List<ComplaintDetails> complaints = complaintsRepo.findAll();
    model.addAttribute("complaints", complaints);
    return "adminComplaints";
}

@GetMapping("/complaints/update/{id}")
public String showUpdateForm(@PathVariable Long id, Model model) {
    ComplaintDetails complaint = complaintsRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid complaint Id:" + id));
    model.addAttribute("complaint", complaint);
    return "updateComplaint";
}

@PostMapping("/complaints/update")
public String updateComplaintStatus(@RequestParam Long id, @RequestParam String status) {
    ComplaintDetails complaint = complaintsRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid complaint Id:" + id));
    complaint.setStatus(status);
    complaintsRepo.save(complaint); // Save the updated complaint
    return "redirect:/admincomplaints"; // Redirect to admin complaints page
}



}
