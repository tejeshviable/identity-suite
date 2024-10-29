package com.ioh.dto;

import lombok.Data;

import java.util.List;

@Data
public class SilentAuthRequestDTO {
    private String brand;
    private List<Workflow> workflow;

    @Data
public static class Workflow{
    private String to;
    private String channel;
    private boolean sandbox;

}
}
