package com.example.demo;

import java.io.Serializable;

public record Recipe(Long id, String title, String image)
implements Serializable {
    private static final long serialVersionUID = 1L; // Optional but recommended for version control
}
