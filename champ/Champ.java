package champ;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Champ - a robot by (Champ)
 */
public class Champ extends AdvancedRobot {
	boolean movingForward; 
	boolean inWall;
    double previousEnergy = 100;
	int movementDirection = 1;
		int turnDirection = 1;
                       

	public void run() {

		setColors();

		
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

	
		if (getX() <= 50 || getY() <= 50
				|| getBattleFieldWidth() - getX() <= 50
				|| getBattleFieldHeight() - getY() <= 50) {
			this.inWall = true;
		} else {
			this.inWall = false;
		}
		
    
	

		setAhead(40000); 
		setTurnRadarRight(360); 
		this.movingForward = true; 

		while (true) {
		
		
			if (getX() > 50 && getY() > 50
					&& getBattleFieldWidth() - getX() > 50
					&& getBattleFieldHeight() - getY() > 50
					&& this.inWall == true) {
				this.inWall = false;
			}
			if (getX() <= 50 || getY() <= 50
					|| getBattleFieldWidth() - getX() <= 50
					|| getBattleFieldHeight() - getY() <= 50) {
				if (this.inWall == false) {
					reverseDirection();
					inWall = true;
				}
			}

		
			if (getRadarTurnRemaining() == 0.0) {
				setTurnRadarRight(360);
			}

			execute();
		}
	}

	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}

	public void onScannedRobot(ScannedRobotEvent e) {
	
		double absoluteBearing = getHeading() + e.getBearing();

	
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing
				- getGunHeading());
		double bearingFromRadar = normalRelativeAngleDegrees(absoluteBearing
				- getRadarHeading());

		
		if (this.movingForward) {
			setTurnRight(normalRelativeAngleDegrees(e.getBearing() + 80));
		} else {
			setTurnRight(normalRelativeAngleDegrees(e.getBearing() + 100));
		}

	
		if (Math.abs(bearingFromGun) <= 4) {
			setTurnGunRight(bearingFromGun); 
			setTurnRadarRight(bearingFromRadar); 

		
			if (getGunHeat() == 0 && getEnergy() > .2) {
				fire(Math.min(
						4.5 - Math.abs(bearingFromGun) / 2 - e.getDistance() / 250, 
						getEnergy() - .1));
			}
		}
		else {
			setTurnGunRight(bearingFromGun);
			setTurnRadarRight(bearingFromRadar);
		}

	
		if (bearingFromGun == 0) {
			scan();
		}
		double changeInEnergy =
      previousEnergy-e.getEnergy();
 if (changeInEnergy>0 &&
        changeInEnergy<=3) {
        movementDirection =-movementDirection;
         setAhead((e.getDistance()/4+25)*movementDirection);
     }
	  previousEnergy = e.getEnergy();
	 
	}


	public void onHitRobot(HitRobotEvent e) {
	if (e.isMyFault()) {
	reverseDirection();
		}
		
	}

	private void setColors() {
		setBodyColor(Color.BLACK);
		setGunColor(Color.BLACK);
		setRadarColor(Color.RED);
		setBulletColor(Color.GRAY);
		setScanColor(Color.GRAY);
	}


	public void reverseDirection() {
		if (this.movingForward) {
			setBack(40000);
			this.movingForward = false;
		} else {
			setAhead(40000);
			this.movingForward = true;
		}
	}
}