package com.colorlaboratory.serviceportalbackend.mapper.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IssueMapper {
    IssueDto toDto(Issue issue);
    Issue toEntity(IssueDto issueDto);
}
