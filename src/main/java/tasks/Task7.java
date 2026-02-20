package tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import common.Company;
import common.Vacancy;

/*
Из коллекции компаний необходимо получить всевозможные различные названия вакансий
 */
public class Task7 {

  public static Set<String> vacancyNames(Collection<Company> companies) {
    return new ArrayList<>(companies).stream().flatMap(company -> company.getVacancies().stream())
    .map(Vacancy::getTitle).collect(Collectors.toSet());
  }

}