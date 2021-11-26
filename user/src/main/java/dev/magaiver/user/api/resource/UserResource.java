package dev.magaiver.user.api.resource;


import dev.magaiver.user.api.model.filter.UserEmailFilter;
import dev.magaiver.user.api.model.input.UserInput;
import dev.magaiver.user.api.model.input.UserPasswordInput;
import dev.magaiver.user.api.model.output.UserOutput;
import dev.magaiver.user.domain.model.User;
import dev.magaiver.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResource {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutput register(@Valid @RequestBody final UserInput userInput) {
        final User userSaved = userService.save(userInput.assembler());
        return new UserOutput().disassembler(userSaved);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordInput userPasswordModel) {
        userService.changePassword(id, userPasswordModel.getPassword(), userPasswordModel.getCurrentPassword());
    }

    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public UserOutput findByEmail(@Valid @RequestBody final UserEmailFilter userEmailFilter) {
        return new UserOutput().disassembler(userService.findByEmail(userEmailFilter.getEmail()));
    }
}
