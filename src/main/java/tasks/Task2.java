package tasks;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

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
    return Stream.of(persons1, persons2).flatMap(Collection::stream)
            .sorted(Comparator.comparing(Person::createdAt)).limit(limit).toList();
  }
}

