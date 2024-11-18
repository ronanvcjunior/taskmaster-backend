package com.ronanvcjunior.taskmaster.domains;

import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskSpecification {

    public static Specification<TaskEntity> byFilters(String filters, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("owner").get("id"), userId));

            if (filters != null && !filters.isEmpty()) {
                Map<String, String> filterMap = parseFilters(filters);
                filterMap.forEach((key, value) -> {
                    predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                });
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Map<String, String> parseFilters(String filters) {
        return Arrays.stream(filters.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }
}
