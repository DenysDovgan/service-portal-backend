package com.colorlaboratory.serviceportalbackend.model.dto.issue.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class AssignTechnicianRequest {
    @NotEmpty
    private List<@NotNull Long> technicianIds;
}
