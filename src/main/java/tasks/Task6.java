package tasks;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.Area;
import common.Person;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    Map<Integer, String> areaIdToName = areas.stream().collect(Collectors.toMap(Area::getId, Area::getName));
    
    return persons.stream().flatMap(person ->
      personAreaIds.get(person.id()).stream().map(area_id -> person.firstName() + " - " + areaIdToName.get(area_id))
    ).collect(Collectors.toSet());
  }
}