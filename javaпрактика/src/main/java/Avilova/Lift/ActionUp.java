package Avilova.Lift;

public class ActionUp implements IAction{

    /**лифт, для которого осуществляется движение*/
    private Lift lift;

    /**
     * конструктор
     * @param lift-лифт
     */
    ActionUp(Lift lift){
        this.lift = lift;
    }

    /**
     * движение лифта, подходит для тех, кто движется вверх, сначала пассажиры выходят, потом заходят,
     * если паасажир отказался, т.е. isSuit = false или грузоподъемность не позвоняет ему зайти,
     * то поле isLater помечается true и лифт приедет за ним, но уже в следующий раз.
     * Лифт опускается, если пассажир находится ниже, чем лифт,
     * иначе поднимается, если пассажир находится выше лифта
     * @param number-этаж назначения
     */
    public void run(int number) {
        int location = lift.getLocation();

        if (location == number)
            //зайдут
            for (int i = 0; i < lift.passengerList.size(); i++) {
                if ((lift.passengerList.get(i).getFloorOfDeparture() - lift.passengerList.get(i).getFloorOfDestination() < 0) &&
                        (lift.passengerList.get(i).getFloorOfDeparture() == lift.getLocation()) && (!lift.passengerList.get(i).getIsInLift()))

                    if (lift.passengerList.get(i).isSuit() && (lift.getVol() - lift.colOfPassangeInLift() >= 1))
                        lift.passengerList.get(i).setIsInLift(true);
                    else
                        lift.passengerList.get(i).setIsLater(true);

                number = find();
            }

        if ((location != lift.getPossibleFinaleFloor()) && (location < number)) {

            lift.setLocation(++location);

            if (lift.getLocation() == number) {

                while (anyoneElse()) {
                    //выходят
                    for (int i = 0; i < lift.passengerList.size(); i++)
                        if ((lift.passengerList.get(i).getIsInLift()) &&
                                (lift.passengerList.get(i).getFloorOfDestination() == lift.getLocation()))
                            lift.passengerList.remove(lift.passengerList.get(i));
                }

                //заходят
                for (int i = 0; i < lift.passengerList.size(); i++)
                    if ((lift.passengerList.get(i).getFloorOfDeparture() - lift.passengerList.get(i).getFloorOfDestination() < 0) &&
                            (lift.passengerList.get(i).getFloorOfDeparture() == lift.getLocation()) && (!lift.passengerList.get(i).getIsInLift())) {

                        if (lift.passengerList.get(i).isSuit() && (lift.getVol() - lift.colOfPassangeInLift() >= 1))
                            lift.passengerList.get(i).setIsInLift(true);
                        else
                            lift.passengerList.get(i).setIsLater(true);
                    }
            }
        } else if (location > number) {
            --location;
            lift.setLocation(location);
        }
    }

    /**
     * найти этаж назначения, на котором кто-либо выходит или заходит
     * и расстояние до него самое наименьшее
     * @return
     */
    public int find(){
        int min = lift.getPossibleFinaleFloor();

        for (int i = 0; i < lift.passengerList.size(); i++) {
            if ((!lift.passengerList.get(i).getIsInLift()) && (lift.passengerList.get(i).getFloorOfDeparture() - lift.passengerList.get(i).getFloorOfDestination() < 0)
                    && (lift.getVol() - lift.colOfPassangeInLift() >= 1) && (!lift.passengerList.get(i).getIsLater())
                    && (min > lift.passengerList.get(i).getFloorOfDeparture()))
                min = lift.passengerList.get(i).getFloorOfDeparture();
            if (lift.passengerList.get(i).getIsInLift() && (min > lift.passengerList.get(i).getFloorOfDestination()))
                min = lift.passengerList.get(i).getFloorOfDestination();
        }
        return min;
    }

    /**
     * посмотеть, есть ли еще пассажиры, к которым лифт не приезжал
     * @return true-есть пассажиры, false-нет пассажиров
     */
    public boolean anyoneElse(){
        for (int i = 0; i < lift.passengerList.size(); i++)
        if ((lift.passengerList.get(i).getIsInLift()) &&
                (lift.passengerList.get(i).getFloorOfDestination() == lift.getLocation()))
            return  true;
        return  false;
    }
}
