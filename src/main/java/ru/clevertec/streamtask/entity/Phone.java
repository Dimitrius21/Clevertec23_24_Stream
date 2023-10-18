package ru.clevertec.streamtask.entity;

public class Phone {
    private Operator operator;
    private String number;

    public Operator getOperator() {
        return operator;
    }

    public String getNumber() {
        return number;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("(%d)%s", operator.getCode(), number);
    }

    public static Phone getRandomPhone(){
        Phone phone = new Phone();
        int number = (int) (Math.random()*3);
        Operator operator = switch (number){
            case 0 -> Operator.A1;
            case 1 -> Operator.MTS;
            case 2 -> Operator.LIFE;
            default -> null;
        };
        phone.setOperator(operator);
        String phoneNumber = "";
        for (int i=1; i<=7; i++){
            phoneNumber+=(int) (Math.random()*10);
        }
        phone.setNumber(phoneNumber);
        return phone;
    }
}
