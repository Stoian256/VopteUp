package com.java.voteup.service;


import com.java.voteup.domain.User;
import com.java.voteup.dto.LoginDto;
import com.java.voteup.dto.UserDto;
import com.java.voteup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.java.voteup.utils.Utils.sha256;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void userRegistration(User user) {
        user.setValidated(false);
        userRepository.save(user);
    }

    public UserDto getUserInfoById(String userId) {
        User user = userRepository.findByUserIdEquals(Integer.parseInt(userId));
        return new UserDto(user.getFirstName(), user.getLastName(), user.getCnp(), user.getEmail());
    }

    public Integer userLogin(LoginDto loginDTO) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmailLike(loginDTO.getUsername());

        Integer Y = Integer.parseInt(user.getPassword());

        BigInteger base = new BigInteger(Y.toString());
        BigInteger exponent = new BigInteger(loginDTO.getC().toString()); // Exponent
        BigInteger modulus = new BigInteger("1000000007"); // Modul

        BigInteger T2prim = base.modPow(exponent, modulus);

        base = new BigInteger(user.getPublicKey().toString());
        exponent = new BigInteger(loginDTO.getZx().toString());
        BigInteger T2second = base.modPow(exponent, modulus);
        BigInteger T2 = T2prim.multiply(T2second);
        T2 = T2.mod(modulus);


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String combinat = Y.toString() + T2.toString();

        BigInteger crez = new BigInteger(sha256(combinat), 16);

        if (crez.toString().equals(loginDTO.getC().toString()))
            return user.getUserId();
        else
            return null;
    }

    public Integer getPublicKeyByEmail(String email) {
        return userRepository.findByEmailLike(email).getPublicKey();
    }

    public Boolean checkValidate(String userId) {
        return userRepository.findByUserIdEquals(Integer.parseInt(userId)).getValidated();
    }

    public void updateUser(String userId, UserDto updatedUser) {
        User existingUser = userRepository.findByUserIdEquals(Integer.parseInt(userId));
        if (existingUser != null) {
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getCnp() != null) {
                existingUser.setCnp(updatedUser.getCnp());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            userRepository.save(existingUser);
        }
    }

}
