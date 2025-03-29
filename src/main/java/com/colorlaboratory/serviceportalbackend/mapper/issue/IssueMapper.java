package com.colorlaboratory.serviceportalbackend.mapper.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IssueMapper {

    @Mapping(target = "createdBy", expression = "java(issue.getCreatedBy().getId())")
    IssueDto toDto(Issue issue);

    @Mapping(target = "createdBy", expression = "java(issue.getCreatedBy().getId())")
    List<IssueDto> toDto(List<Issue> issues);
}
