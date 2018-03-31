/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant;

import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class ListPosition {

    private static ListPosition instance;
    private static HashMap<Integer, StatePosition> posMap = new HashMap<Integer, StatePosition>();

    private ListPosition() {        
        init();                        
    }
    
    public StatePosition getStatePosistion(int region){
        return posMap.get(region);
    }
    
    public static ListPosition getInstance(){
        if(instance == null){
            instance = new ListPosition();
        }
        return instance;        
    }
    
    public static void init() {
        posMap.put(0, new StatePosition(0, 0.0, 0.0));
        posMap.put(1, new StatePosition(1, 21.03333, 105.85000));
        posMap.put(2, new StatePosition(2, 21.18528, 106.05639));
        posMap.put(3, new StatePosition(3, 20.5835196, 105.92299));
        posMap.put(4, new StatePosition(4, 20.9385958, 106.3206861));
        posMap.put(5, new StatePosition(5, 20.86194, 106.68028));
        posMap.put(6, new StatePosition(6, 20.8525711, 106.0169971));
        posMap.put(7, new StatePosition(7, 20.2791804, 106.2051484));
        posMap.put(8, new StatePosition(8, 20.2129969, 105.92299));
        posMap.put(9, new StatePosition(9, 20.5386936, 106.3934777));
        posMap.put(10, new StatePosition(10, 21.3608805, 105.5474373));
        posMap.put(11, new StatePosition(11, 22.7662056, 104.9388853));
        posMap.put(12, new StatePosition(12, 22.635689, 106.2522143));
        posMap.put(13, new StatePosition(13, 22.3032923, 105.876004));
        posMap.put(14, new StatePosition(14, 21.8563705, 106.6291304));
        posMap.put(15, new StatePosition(15, 22.1726708, 105.3131185));
        posMap.put(16, new StatePosition(16, 21.5671559, 105.8252038));
        posMap.put(17, new StatePosition(17, 21.268443, 105.2045573));
        posMap.put(18, new StatePosition(18, 21.3014947, 106.6291304));
        posMap.put(19, new StatePosition(19, 21.006382, 107.2925144));
        posMap.put(20, new StatePosition(20, 22.3380865, 104.1487055));
        posMap.put(21, new StatePosition(21, 21.6837923, 104.4551361));
        posMap.put(22, new StatePosition(22, 21.8042309, 103.1076525));
        posMap.put(23, new StatePosition(23, 20.6861265, 105.3131185));
        posMap.put(24, new StatePosition(24, 22.3686613, 103.3119085));
        posMap.put(25, new StatePosition(25, 21.1022284, 103.7289167));
        posMap.put(26, new StatePosition(26, 20.1291279, 105.3131185));
        posMap.put(27, new StatePosition(27, 19.2342489, 104.9200365));
        posMap.put(28, new StatePosition(28, 18.2943776, 105.6745247));
        posMap.put(29, new StatePosition(29, 17.6102715, 106.3487474));
        posMap.put(30, new StatePosition(30, 16.7943472, 106.963409));
        posMap.put(31, new StatePosition(31, 16.467397, 107.5905326));
        posMap.put(32, new StatePosition(32, 16.05194, 108.21528));
        posMap.put(33, new StatePosition(33, 15.5393538, 108.019102));
        posMap.put(34, new StatePosition(34, 15.0759838, 108.7125791));
        posMap.put(35, new StatePosition(35, 14.1665324, 108.902683));
        posMap.put(36, new StatePosition(36, 13.0881861, 109.0928764));
        posMap.put(37, new StatePosition(37, 12.2585098, 109.0526076));
        posMap.put(38, new StatePosition(38, 11.6738767, 108.8629572));
        posMap.put(39, new StatePosition(39, 11.0903703, 108.0720781));
        posMap.put(40, new StatePosition(40, 14.661167, 107.83885));
        posMap.put(41, new StatePosition(41, 13.8078943, 108.109375));
        posMap.put(42, new StatePosition(42, 12.7100116, 108.2377519));
        posMap.put(43, new StatePosition(43, 12.2646476, 107.609806));
        posMap.put(44, new StatePosition(44, 11.5752791, 108.1428669));
        posMap.put(45, new StatePosition(45, 10.83333, 106.63278));
        posMap.put(46, new StatePosition(46, 10.03278, 105.78389));
        posMap.put(47, new StatePosition(47, 11.7511894, 106.7234639));
        posMap.put(48, new StatePosition(48, 11.3254024, 106.477017));
        posMap.put(49, new StatePosition(49, 10.95694, 106.84306));
        posMap.put(50, new StatePosition(50, 11.3675415, 106.1192802));
        posMap.put(51, new StatePosition(51, 10.5417397, 107.2429976));
        posMap.put(52, new StatePosition(52, 10.695572, 106.2431205));
        posMap.put(53, new StatePosition(53, 10.4937989, 105.6881788));
        posMap.put(54, new StatePosition(54, 10.4493324, 106.3420504));
        posMap.put(55, new StatePosition(55, 10.5215836, 105.1258955));
        posMap.put(56, new StatePosition(56, 10.1081553, 106.4405872));
        posMap.put(57, new StatePosition(57, 10.0861281, 106.0169971));
        posMap.put(58, new StatePosition(58, 9.812741, 106.2992912));
        posMap.put(59, new StatePosition(59, 9.757898, 105.6412527));
        posMap.put(60, new StatePosition(60, 9.8249587, 105.1258955));
        posMap.put(61, new StatePosition(61, 9.6003688, 105.9599539));
        posMap.put(62, new StatePosition(62, 9.2515555, 105.5136472));
        posMap.put(63, new StatePosition(63, 8.9624099, 105.1258955));

        
    }
}
