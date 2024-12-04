package com.example.billboard.model;

import lombok.Data;

import java.util.List;


@Data
public class Server {
    public static final Server INSTANCE = new Server();
    public final Integer capacityGB = 32;
    public List<Advertisement> advertisements;
}
