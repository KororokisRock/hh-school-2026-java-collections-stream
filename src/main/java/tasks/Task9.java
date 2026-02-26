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
  // убрали distinct так как и так остаются только уникальные элементы при заворачивании в Set
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNames(persons).stream().collect(Collectors.toSet());
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  // засовываем поля Person в stream после убираем null по фильтру и объединяем в строку с разделителем " " при помощи Collectors.joining()
  // меньший объем кода, при этом сохранилась надеждность и читаемость
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName()).filter(Objects::nonNull).collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  // более читаемый и в меньшем объёме код, создаем необъодимую Map при помощи Collectors.toMap() разрешаем конфликты при одинаковых id при помощи merge функции
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toMap(Person::id, Person::firstName, (person1, person2) -> person1));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  // из коллекции persons1 запустили stream и проверили чтобы хоть один элемент из persons1 находился в persons2
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().anyMatch(person -> persons2.contains(person)); // anyMatch возвращает true если есть хоть один из потока persons1, находящийся в persons2
  }

  // Посчитать число четных чисел
  // вместо использование счетчика просто вызовем метод count потока для подсчета кол-ва чиседл
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  // При сохдании HashSet внутри него также создается HashMap в которой сами значения хранятся как ключи, а в качестве значений пучтые объекты.
  // После при распределнии значений в HashSet происходит следующее: так как у нас 10000 чисел (от 1 до 10000) то коныеный размер таблицы в hashMap будет 16384.
  // РАспределение же по таблице происходит по следующей формуле: (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16); причем hashCode key,
  // то есть самих наших чисел будет всегда равен их значению.
  // Также стоит учесть, что (h >>> 16) просто смешивает старшие биты с младшими битами числа через XOR чтобы они тоже влияли на итоговый hash,
  // но так как в нашем случаи числа до 10000, то в старших 16 битах все нули, и итоговый hash остается самим значением числа, то есть у 1 - hash=1, у 2 - hash=2 и т.д.
  // далее индекс для самой таблицы также выбирается по формуле (n - 1) & hash, в данном случаи (n - 1) просто является ограничением на размер таблицы
  // (т.к. таблица всегда размером 2 в какой-то степени, поэтому двоичное представление размера
  // будет сколько-то 1 подряд и у всех чисел выходящих за данный размер старшие биты занулятся)
  // поэтому индекс в таблице у данных чисел будет равен им же самим. Таким образом числа по таблице будут распределены равномерно
  // и число 1 займет ячейку 1, число 2 - ячейку 2 и т.д.
  // Теперб при вызове метода toString() у HashSet() он вызове у себя методы keySet().iterator() у своей hashMap. keySet() возвращает
  // класс KeySet у которого метод iterator() возвращает класс KeyIterator. keyIterator являясь потомком HashIterator использует в методе next() и
  // других своих методах метод nextNode() который просто проходит по таблице HashMap возвращает следующий объект Node (реализация Map.Entry),
  // и от этого объекта он возвращает key, то есть само наше значение. В итоге у нас просто значения распределяются последовательно по таблице HashMap и в методе toString()
  // мы последовательно по ячейкам таблицы прохоимся и получаем необходимые значения
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Set<Integer> set = new HashSet<>(integers);
    System.out.println("Хеш-коды объектов Integer:");

    for (int i = 0; i < snapshot.size(); i++) {
      int h = snapshot.get(i).hashCode();
      int hash = h ^ (h >>> 16);
      System.out.println(snapshot.get(i) + " - " + Integer.toBinaryString(snapshot.get(i)) + " " + hash + " - " + Integer.toBinaryString(hash));
    }
    // цикл вывделет следующее:
    // Integer  hash
    // 1 - 1    1 - 1
    // 2 - 10   2 - 10
    // 3 - 11   3 - 11
    // 4 - 100  4 - 100
    // 5 - 101  5 - 101
    // 6 - 110  6 - 110
    // ...
    // 101 - 1100101  101 - 1100101
    // ...

    assert snapshot.toString().equals(set.toString());
  }
}



// static final int hash(Object key) {
//     int h;
//     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
// }



// (n - 1) & hash