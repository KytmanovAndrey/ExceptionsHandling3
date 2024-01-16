// Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке,
//        разделенные пробелом: Фамилия Имя Отчество, дата_рождения,номер_телефона,пол
//        Форматы данных:фамилия,имя, отчество - строки дата_рождения- строка формата dd.mm.yyyy
//        номер_телефона - целое беззнаковое число без форматирования пол - символ латиницей f или m.
//        Критерии:
//         Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым,
//        вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше
//        данных, чем требуется.
//         Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
//        Если форматы данных не совпадают,нужно бросить исключение, соответствующее типу проблемы. Можно
//        использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано,
//        пользователю выведено сообщение с информацией, что именно неверно.
//         Если всё введено и обработано верно, должен создаться файл с названием,равным фамилии, в него в одну
//        строку должны записаться полученные данные, вида.
//
//  <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
//
//  Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
//
//        Не забудьте закрыть соединение с файлом.
//
//        При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
//
//        Данная промежуточная аттестация оценивается по системе "зачет" / "не зачет"
//
//        "Зачет" ставится, если слушатель успешно выполнил
//        "Незачет"" ставится, если слушатель успешно выполнил
//
//        Критерии оценивания:
//        Слушатель напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробело

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataCollectorApp {
    public static void main(String[] args) {
        String[] messages = new String[]{"Фамилия", "Имя", "Отчество", "Дата рождения(dd.mm.yyyy)", "Номер телефона", "Пол(f или m)"};
        List<String> list = new ArrayList<>();
        Collections.addAll(list, messages);
        Collections.shuffle(list);

        System.out.println("Введите следующие данные через пробел: " + String.join("; ", list));
        Scanner scanner = new Scanner(System.in, "ibm866");
        String inputStr = scanner.nextLine();
        String[] inputArr = inputStr.split(" ");
        int numberCode = checkArray(inputArr, messages.length);
        printString(numberCode);
        if (numberCode == 0) recordToFile(list, inputArr);
        scanner.close();
    }

    public static int checkArray(String[] arr, int count){
        if (arr.length > count)
            return -2;
        else if (arr.length < count)
            return -1;
        else
            return 0;
    }

    public static void printString(int number){
        if (number == -1)
            System.out.println(number + "\nВы ввели меньше данных чем требуется");
        else if (number == -2)
            System.out.println(number + "\nВы ввели больше данных чем требуется");
    }

    public static void recordToFile(List<String> dataRequest, String[] input) {
        String[] record = new String[dataRequest.size()];
        for (int i = 0; i < input.length; i++) {
            if  (dataRequest.get(i).equals("Фамилия")) {
                record[0] = input[i];
                if (!input[i].matches("[\\D]+")) {
                    throw new RuntimeException("В фамилии число!");
                }
            } else if (dataRequest.get(i).equals("Имя")) {
                if (!input[i].matches("[\\D]+")) {
                    throw new RuntimeException("В имени число!");
                }
                record[1] = input[i];
            } else if (dataRequest.get(i).equals("Отчество")) {
                if (!input[i].matches("[\\D]+")) {
                    throw new RuntimeException("В отчестве число!");
                }
                record[2] = input[i];
            } else if (dataRequest.get(i).equals("Дата рождения(dd.mm.yyyy)")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                dateFormat.setLenient(false);
                try {
                    dateFormat.parse(input[i]);
                } catch (ParseException e) {
                    throw new RuntimeException("дата введена некорректно");
                }
                record[3] = input[i];
            } else if (dataRequest.get(i).equals("Номер телефона")) {
                try {
                    Long.parseLong(input[i]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("в номере телефона некорректный ввод");
                }
                record[4] = input[i];
            } else if (dataRequest.get(i).equals("Пол(f или m)")) {
                if (!input[i].equals("f") && !input[i].equals("m")) {
                    throw new RuntimeException("Пол может быть лишь f или m");
                }
                record[5] = input[i];
            }
        }

        try (FileWriter fw = new FileWriter(String.format("%s.txt", record[0]), true)) {
            fw.write(String.join(" ", record) + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл или имя файла содержит недопустимые символы");
            e.printStackTrace();
        }
    }
}