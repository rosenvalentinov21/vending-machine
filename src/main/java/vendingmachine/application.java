package vendingmachine;

import vendingmachine.controller.MachineController;

//TODO Why does the class name start with a lowercase letter?
public class application {
    public static void main(String[] args) {
        final MachineController machineController = new MachineController();

        machineController.showMenu();
    }
}
