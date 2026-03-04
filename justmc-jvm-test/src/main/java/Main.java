import justmc.Player;
import justmc.annotation.EventHandler;
import justmc.event.player.PlayerSwapHandItemsEvent;


public final class Main {
    static void println(String message) {
        Player.ALL.sendMessage(message);
    }

    @EventHandler
    static void onSwapHands(PlayerSwapHandItemsEvent event) {
        calcSumArr(new int[]{1,2,3,4,5});
    }

    static int calcSumArr(int[] arr) {
        int s = 0;
        int i = 0;
        int l = arr.length;
        println("start");
        while (i < l) {
            println("in");
            s += arr[i];
            ++i;
        }
        println("end");
        return s;
    }
    
//    @EventHandler(id = "player_swap_hands")
//    public static void arr() { // наверное работает
//        var list = CopyableList.of(1, 2, 3, 4);
//        Player.DEFAULT.sendMessage(Util.asString(list)); 
//    }

//    public static int counter = 0; // наверное работает
//
//    @EventHandler
//    public static void onLeftClick(PlayerLeftClickEvent event) { // наверное работает
//        if (event.hasBlock()) {
//            event.getPlayer().sendMessage("has block");
//        } else if (!event.hasBlock()) {
//            event.getPlayer().sendMessage("no has block");
//        } else {
//            event.getPlayer().sendMessage("end");
//        }
//        event.cancel();
    ////        CopyableList<Integer> list = CopyableList.of();
    ////        list = list.add(2);
    ////        event.getPlayer().sendMessage(list.get(0).toString());
//        // Player player = event.getPlayer(); // запрещено TODO сделать как инлайн переменные (без этого Variable не будет работать)
//        //StringConcatFactory.makeConcatWithConstants(null, "", MethodType.methodType(String.class), "recipe", "Click ", counter).dynamicInvoker().invoke(1);
//        //event.getPlayer().sendMessage("msg: " + ++counter);
//    }

//    void a() {
//        byte a = 2;
//        a += 3;
//    }

//    static void branches(int a) { // вроде работает
//        println("start");
//        if (a < 5) {
//            println("a < 5");
//            if (a < 4) {
//                println("a < 4");
//            } else {
//                println("else 'a < 4'");
//            }
//        } else {
//            println("else 'a < 5'");
//        }
//        println("inter");
//        if (a < 3) {
//            println("a < 3");
//        } else {
//            println("else 'a < 3'");
//        }
//        println("anyway");
//    }

//    static void and(int i) {
//        println("pre anyway");
//        if (i < 2) {
//            println("i < 2");
//        } else if (i > 4 && i < 7) { // не работает
//            println("i > 4 && i < 7");
//        } else if (i < 9) {
//            println("i < 9");
//        } else {
//            println("else");
//        }
//        println("anyway");
//    }

//    static void cycle() { // наверное работает
//        int i = 0;
//        println("start");
//        while (i < 10) {
//            println("pre anyway");
//            if (i < 5) {
//                println("i < 5");
//            } else if (i < 7) {
//                println("i < 7");
//            } else if (i < 9) {
//                println("i < 9");
//            } else {
//                println("else");
//            }
//            println("anyway");
//            i++;
//        }
//        println("end");
//    }

//    void label() { // не работает
//        boolean t = true;
//        first:
//        {
//            second:
//            {
//                third:
//                {
//                    System.out.println("before break");
//                    if (t) {
//                        break second; // выход из блока second (не работает)
//                    }
//                    System.out.println("unreachable 1");
//                }
//                System.out.println("unreachable 2");
//            }
//            System.out.println("after second");
//        }
//    }
//
    // INVOKEDYNAMIC только для 2 конструкций: лямбды и конкатенация (не работает)
//    void runnable() {
//        Util.measureNanoTime(() -> {
//            int a = 1;
//            a++;
//        });
//    }
//
//    static long methodWithParams(int i, Object o, long l) {
//        // GC. (его пока нет)
//        long r = i + l;
//        String s = o.toString();
//        return r;
//        // refs--
//    }
//
//    static void invokeMethod() {
//        Bebra obj = new Bebra(); // refs++
//        long bebra = methodWithParams(1, obj, 3);
//        // refs--
//    }
    
//    static void tempVar() {
//        //finalMethod();
//        Variable t = Variable.temp();
//        Player.DEFAULT.sendMessage(Util.asString(t));
//        //Util.operation("set_variable_value", CopyableMap.of(new Object[]{}, new Object[]{}));
//    }
//    static void arrays() {
//        int[] arr = new int[100];
//        arr[2] = 2;
//        int s = arr[0];
    ////        for (var e : arr) {
    ////            s += e;
    ////        }
//    }
//
//    static void tryCatch() { // не работает
//        try {
//            System.out.println("try");
//        } catch (Exception e) {
//            System.out.println("catched");
//            throw new RuntimeException(e);
//        } finally {
//            System.out.println("finally");
//        }
//    }
}
