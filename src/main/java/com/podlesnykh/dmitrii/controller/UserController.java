package com.podlesnykh.dmitrii.controller;

import com.podlesnykh.dmitrii.exception.InvalidIdException;
import com.podlesnykh.dmitrii.exception.MultipleUserSameNameException;
import com.podlesnykh.dmitrii.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Map<Integer, String> userIdNameMap = new HashMap<>();

    static {
        userIdNameMap.put(1, "Jane");
        userIdNameMap.put(2, "Jack");
        userIdNameMap.put(3, "Jack");
        userIdNameMap.put(-1000, "Mark");
    }

    @GetMapping("/{name}")
    public String getInfo(@PathVariable("name") String name) {
        if (!userIdNameMap.containsValue(name)) {
            throw new UserNotFoundException();
        }
        long countRecords = userIdNameMap.values().stream()
                .filter(s -> s.equalsIgnoreCase(name))
                .count();
        if (countRecords > 1) {
            throw new MultipleUserSameNameException();
        }

        Integer result = userIdNameMap.entrySet().stream()
                .filter(e -> e.getValue().equalsIgnoreCase(name))
                .map(Map.Entry::getKey)
                .findFirst().orElse(-100);

        if (result < 0) {
            throw new InvalidIdException();
        }
        return String.valueOf(result);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleExceptions(UserNotFoundException e)
    {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("USER NOT FOUND!!!!!!!!");
    }


}
