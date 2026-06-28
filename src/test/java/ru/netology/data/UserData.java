package ru.netology.data;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserData {
    private String city;
    private String name;
    private String phone;
    private String date;
}