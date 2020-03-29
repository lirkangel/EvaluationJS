import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
/*

Ví dụ:
// data dạng json
data = {
	"price": 100,
	"amount": 50,
	"discount": 0.5,
	"unit": 1000,
	"added_tax": 50000
}

calc("price * amount * discount", data) -> trả ra 2500
calc("price * amount * discount + 1000", data) -> trả ra 3500
calc("price * amount * unit", data) -> trả ra 5000000
calc("discount * 1000 + amount * price", data) -> trả ra 5500

// nếu có hỗ trợ dấu ngoặc
calc("(price * amount + added_tax) * (1 - discount)", data) -> trả ra 27500
calc("(price * amount + added_tax) * (1 - discount) / unit", data) -> trả ra 27,500


Note:
Các tiêu chí đánh giá là:
- Chương trình hoạt động đúng
- Trình bày gọn gàng sạch đẹp

Sẽ thêm điểm cộng nếu:
- Hỗ trợ dấu ngoặc (điểm cộng)
- Hỗ trợ thêm nhiều loại tính năng (modulo, power, hàm min/max, ...)

Hint:
- Để parse string công thức, có thể nghiên cứu dùng regex
- Để lưu trữ công thức và tính toán, có thể dùng công thức hậu tố (Evaluation postfix expression)

Có thể implement bằng mã giả hoặc bất kì ngôn ngữ nào, quan trọng là ý tưởng của bạn.

 */


public class Main {

    private static Object calc (String formula, HashMap<String,Double> data) throws ScriptException {

        Pattern pattern = Pattern.compile("(?<=[\\(\\)\\+\\-*\\/\\^w])|(?=[\\(\\)\\+\\-*\\/\\^w])");
        List<String> wordList = Arrays.asList(pattern.split(formula));


        for (int i = 0; i < wordList.size();i++) {
            for (String key : data.keySet() ) {
                if ((wordList.get(i).equals(key)))
                    wordList.set(i,data.get(key).toString());
            }
        }
        String s = wordList.stream().map(e -> e).reduce("", String::concat);
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        Object result =engine.eval(s);
        System.out.println("evaluating " + s);
        return result;
    }


    public static void main(String[] args)  {

//        String jsonString = "{\n" +
//                "\"price\":123,\n" +
//                "\"amount\":123\n" +
//                "}";
//        HashMap<String, Double> input = new Gson().fromJson(
//                jsonString, new TypeToken<HashMap<String, Object>>() {}.getType()
//        );


//        {"price":123,"amount":123}
        Boolean flag = true;
        Scanner scanner = new Scanner(System.in);

        HashMap<String, Double> input = new HashMap<>();
        while (flag)
        try {
            System.out.println("Input Json String with bracket in one line");
            String inputJson = scanner.nextLine();
            inputJson = inputJson.replaceAll("\\s+","");
            input = new Gson().fromJson(
                    inputJson, new TypeToken<HashMap<String, Object>>() {}.getType()
            );
            flag = false;
        } catch (Exception e) {
            flag = true;
            System.out.println("Cannot parse you data, try again");
        }

        System.out.println("Your data input is: ");
        input.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        System.out.println("Enter formula in one line");
//        "price+amount*(5+2)/10-4" sample
        String formula = scanner.nextLine();
        try {
            Object result =  calc(formula,input);
            System.out.println(result);
        } catch (ScriptException e) {
            System.out.println("Sry cannot calcute with this formula");
        }



    }
}
