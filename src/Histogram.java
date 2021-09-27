import exception.NotInitializedException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Histogram {
    private int divider=10;
    private HashMap<Integer,StringBuilder> histogram;

    public Histogram() {
        this.divider = 10;
        this.histogram = new LinkedHashMap<>();
    }

    public static void main(String[] args) throws NotInitializedException {
        Histogram histogram = new Histogram();
        histogram.makeHistogram();
    }



    public void makeHistogram() throws NotInitializedException {

        String inputdata=readInputData();

        HashMap<String,Integer> amounts=new LinkedHashMap<>();
        // 레이블-도수 셋트별로 split
        String[] pairs=inputdata.split(" ");

        int maxVal=0;
        // 레이블과 도수를 map으로 관리
        for(String pair : pairs){
            String[] splits=pair.split(":");
            int value=Integer.parseInt(splits[1]);
            amounts.put(splits[0],value);
            maxVal=Math.max(maxVal,value);
        }

        makeLeftNumber(maxVal);
        makeLeftLayout();
        makeDataArea(maxVal, amounts);

        histogram.values().stream().forEach(System.out::println);
    }

    private String readInputData(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("히스토그램을 위한 데이터를 입력해주세요.\n입력형식: 레이블1:도수1 레이블2:도수2 ...");
        return scanner.nextLine();
    }

    private void makeDataArea(int maxVal, HashMap<String,Integer> amounts) throws NotInitializedException {
        for(Map.Entry<String,Integer> entry: amounts.entrySet()){
            String label=entry.getKey();
            int ratio=(int)(Math.round(((double)entry.getValue()/maxVal)*10));
            setDataForLabels(label,ratio);
        }
    }

    private void setDataForLabels(String label, int ratio) throws NotInitializedException{
        StringBuilder sb=null;

        for(int pointVal=-1; pointVal<=10; pointVal++){

            checkPreLayout(pointVal);
            sb=histogram.get(pointVal);

            if(pointVal==-1){
                sb.append(' ');
                sb.append(label);
            }
            else if(pointVal==0){
                sb.append("--");
            }
            else if(pointVal<=ratio){
                sb.append(" #");
            }else{
                sb.append("  ");
            }
        }
    }

    private void checkPreLayout(int pointVal) throws NotInitializedException {
        if(!histogram.containsKey(pointVal)){
            throw new NotInitializedException("초기 도수 레이아웃이 셋팅되지 않았습니다.");
        }
    }

    private void makeLeftLayout() throws NotInitializedException {
        for(int pointVal=10; pointVal>=-1; pointVal--){
            checkPreLayout(pointVal);
            StringBuilder sb=histogram.get(pointVal);
            switch (pointVal){
                case -1 -> sb.append(' ');
                case 0 -> sb.append('+');
                default -> sb.append('|');
            }
        }
    }

    private void makeLeftNumber(int maxVal){
        // 중간 값
        int middleVal=(int)(Math.round((double) maxVal/2));

        // 왼쪽 축에 표시하는 숫자의 길이에 따라 숫자가 표시 안되는 선들은 빈칸으로 채워주는 작업을 해야하므로 가장 큰 값의 길이, 중간값의 길이를 구함
        int maxValLength=String.valueOf(maxVal).length();
        int middleValLength=String.valueOf(middleVal).length();
        for(int pointVal=10; pointVal>=-1; pointVal--){
            StringBuilder sb=new StringBuilder();
            int blankLength=0;
            switch (pointVal){
                case 0 -> {
                    blankLength=maxValLength-1;
                    sb.append(0);
                }
                case 5 -> {
                    blankLength=maxValLength-middleValLength;
                    sb.append(middleVal);
                }
                case 10 -> sb.append(maxVal);
                default ->{
                    blankLength=maxValLength;
                }

            }
            for(int i=1; i<=blankLength; i++){
                sb.insert(0,' ');
            }
            sb.append(' ');

            histogram.put(pointVal,sb);
        }
    }
}
