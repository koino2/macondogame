package game.scripts.player.weaponrecording;

import game.scripts.weapons.WeaponScript;
import lib.Script;

import java.util.ArrayList;
import java.util.List;

public class PlayerWeaponRecorder extends Script {

    public List<WeaponScript> weapons = new ArrayList<>();
    public List<SingleWeaponRecorder> weaponRecorders = new ArrayList<>();

    @Override
    public void start() {

    }

    public boolean weaponRecordersContainsWeapon(WeaponScript weapon){
        for (SingleWeaponRecorder weaponRecorder : weaponRecorders){
            if (weaponRecorder.weapon == weapon){
                return true;
            }
        }
        return false;
    }

    public void addWeapons(){
        for (Script script : new ArrayList<>(object.scripts)){
            if (script instanceof WeaponScript){
                if (!weapons.contains(script)){
                    weapons.add((WeaponScript) script);
                }
                if (!weaponRecordersContainsWeapon((WeaponScript) script)){
                    SingleWeaponRecorder weaponRecorder = new SingleWeaponRecorder((WeaponScript) script);
                    weaponRecorders.add(weaponRecorder);
                    object.addScript(weaponRecorder);
                }
            }
        }
    }

    public List<WeaponRecording> getRecording(){
        List<WeaponRecording> weaponRecordings = new ArrayList<>();
        for (SingleWeaponRecorder weaponRecorder : weaponRecorders){
            weaponRecordings.add(weaponRecorder.recording);
        }
        return weaponRecordings;
    }

    public void stopRecording(){
        for (SingleWeaponRecorder weaponRecorder : weaponRecorders){
            weaponRecorder.stopRecording();
        }
    }

    @Override
    public void update(double deltaTime) {
        addWeapons();
    }
}
