package tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import common.Person;

/*
Задача 2
На вход принимаются две коллекции объектов Person и величина limit
Необходимо объеденить обе коллекции
отсортировать персоны по дате создания и выдать первые limit штук.
 */
public class Task2 {

  public static List<Person> combineAndSortWithLimit(Collection<Person> persons1,
                                                     Collection<Person> persons2,
                                                     int limit) {
    List<Person> personsList = new ArrayList<>(persons1);
    personsList.addAll(persons2);
    return personsList.stream().sorted((el1, el2) -> el1.createdAt().compareTo(el2.createdAt())).limit(limit).toList();
  }
}