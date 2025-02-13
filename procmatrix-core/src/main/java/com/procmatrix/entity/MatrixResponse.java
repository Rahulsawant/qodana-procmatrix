package com.procmatrix.entity;

import org.springframework.hateoas.Link;

import java.util.List;

public class MatrixResponse {
    private int[][] content;
    private String message;
    private List<Link> links;

    public MatrixResponse(int[][] content, List<Link> links,String message) {
        this.content = content;
        this.links = links;
        this.message=message;
    }

    // Getters and setters
    public int[][] getContent() {
        return content;
    }

    public void setContent(int[][] content) {
        this.content = content;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

