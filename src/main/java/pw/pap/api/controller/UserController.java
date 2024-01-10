package pw.pap.api.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pw.pap.api.dto.AuthResponseDTO;
import pw.pap.config.UserAuthenticationProvider;
import pw.pap.model.User;
import pw.pap.service.UserService;
import pw.pap.api.dto.UserAndPasswordDTO;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public UserController(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody UserAndPasswordDTO userAndPasswordDTO) {
        try {
            User user = userService.login(userAndPasswordDTO.getName(), userAndPasswordDTO.getPassword());
            AuthResponseDTO authResponse = new AuthResponseDTO(userAuthenticationProvider.generateToken(user), user);
            return ResponseEntity.ok(authResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody UserAndPasswordDTO userAndPasswordDTO) {
        try {
            User user = userService.register(userAndPasswordDTO.getName(), userAndPasswordDTO.getPassword());
            AuthResponseDTO authResponse = new AuthResponseDTO(userAuthenticationProvider.generateToken(user), user);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId, @RequestHeader(name = "Authorization") String token) {
        if(!userAuthenticationProvider.validateToken(token).isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/all")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser, @RequestHeader(name = "Authorization") String token) {
        if(!userAuthenticationProvider.validateToken(token).isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @RequestHeader(name = "Authorization") String token) {
        if(!userAuthenticationProvider.validateToken(token).isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<Boolean> authenticateUser(@RequestBody UserAndPasswordDTO userAndPasswordDTO) {
//        System.out.println("Authenticating user: " + userAndPasswordDTO.getName()); // Print the username being authenticated
//        System.out.println("Password passed: : " + userAndPasswordDTO.getPassword()); // Print the password being authenticated
//        try {
//            User user = userService.findByName(userAndPasswordDTO.getName())
//                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
//            boolean isAuthenticated = userService.authenticateUser(user, userAndPasswordDTO.getPassword());
//            System.out.println("Authentication result for " + userAndPasswordDTO.getName() + ": " + isAuthenticated); // Print the result of authentication
//
//            return new ResponseEntity<>(isAuthenticated, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Error during authentication: " + e.getMessage()); // Print the error message
//            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED); // Return an appropriate status code for unauthorized access
//        }
//    }
}
