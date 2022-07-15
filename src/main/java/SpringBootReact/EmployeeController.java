package SpringBootReact;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    // fetching the list of employees 
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
    	List<Employee> list =  employeeRepository.findAll();
    	list.stream().forEach(System.out::println);
        return list;
    }

    // fetching the employee based on the id 
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
        throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
          .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        System.out.println("Get Employee based on Id "+employee);
        return ResponseEntity.ok().body(employee);
    }
    
    // save method.  creating the employees . 
    @PostMapping("/employees")
    public Employee createEmployee(@Validated @RequestBody Employee employee) {
    	
    	Employee emp = employeeRepository.save(employee);
        System.err.println("Save Employee : "+emp);
        return emp;
    }

    
    // Updating the employee based on the id - first and then save 
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
         @Validated @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
    	
    	// step 1 -first fetch based on id 
        Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        // step 2- set the values after fetching.. 
        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        System.out.println("after updated in the database : "+updatedEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    
    // Based on iddelete employee.  
    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
         throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
       .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        
        
        employeeRepository.delete(employee);
        
        // show some response the browser we are defining the pass the string and boolen through map.  
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }}