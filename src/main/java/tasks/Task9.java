package tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import common.Person;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  public static void main(String[] args) {
      Task9 tsk = new Task9();

      tsk.listVsSet();
  }

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  // сначала мы пропускаем персону заглушку при помощи skip и далее берем имена персон в map, получившееся строки оборачиваем в List
  // лучше код так как меньший объем и при этом сохранилась читаемость и даже при пустом вхожном списке вернет именно пустой, нет необходимости делать отдельную проверку
  public List<String> getNames(List<Person> persons) {
    return persons.stream().skip(1).map(Person::firstName).toList();
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  // код и так понятен и эффективен
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNames(persons).stream().distinct().collect(Collectors.toSet());
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  // засовываем поля Person в stream после убираем null по фильтру и объединяем в строку с разделителем " " при помощи Collectors.joining()
  // меньший объем кода, при этом сохранилась надеждность и читаемость
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName()).filter(Objects::nonNull).collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  // более читаемый и в меньшем объёме код, создаем необъодимую Map при помощи Collectors.toMap()
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toMap(Person::id, Person::firstName));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  // из коллекции persons1 запустили stream и проверили чтобы каждый элемент из persons1 находился в persons2
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().allMatch(person -> persons2.contains(person)); // AllMatch возвращает true если для всех элементв выполняется данное условие иначе false
  }

  // Посчитать число четных чисел
  // вместо использование счетчика просто вызовем метод count потока для подсчета кол-ва чиседл
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  // потому что согласно документации метод hashCode() объектов типа Integer возвращает то значение которое хранится в примитиве данного объекта (то есть само число),
  // так как метод hashCode() используется для распределения объектов в HashSet по ячейкам внутренней HashMap
  // то мы видим что числа от 1 до n-го распределяются в соответствующие ячейки от 1 до n-ой соответственно
  // и при выводе методом toString при помощи итератора поочередно просматриваются ячейки от 1 до n-ой и собираются в строку
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Set<Integer> set = new HashSet<>(integers);
    System.out.println("Хеш-коды объектов Integer:");
    snapshot.stream().forEach(number -> System.out.println(number + " " + number.hashCode()));
    assert snapshot.toString().equals(set.toString());
  }
}