package vendingmachine;

import vendingmachine.controller.MachineController;

public class application {
    public static void main(String[] args) {
        MachineController machineController = new MachineController();

        machineController.showMenu();
    }
}
