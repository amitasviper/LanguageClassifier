package in.problem.entrypoint;

import in.problem.entity.Army;
import in.problem.entity.Battalion;
import in.problem.entity.Battalion.Property;
import in.problem.entity.Player;
import in.problem.handler.PlayerManager;
import in.problem.handler.PropertyManager;
import in.problem.substitution.rules.ByHierarchy;

import java.util.Comparator;

/**
 * 
 * This class serves as the entry point to initalise and start the game . The game can be run dynamically where the game rules and variables
 * are parameterised .This is done so that the substitution rule like choosing the lower rank first , looking at adjacent battalions
 * can be changed dynamically at runtime .
 * This is done with the help of the {@link PropertyManager} . The players list are maintained by
 * the {@link PlayerManager}. This is an adoption of the Strategy design pattern. 
 *
 */
public class BattleArena {



    private PlayerManager playerManager;
    private PropertyManager propertyManager;
    public BattleArena(){

        playerManager = new PlayerManager();
        propertyManager = new PropertyManager(playerManager);
    }
    
	public void battlePlayers(Player mainPlayer,Player enemyPlayer){
		
		Army mainArmy = mainPlayer.getArmy();
		Army enemyArmy = enemyPlayer.getArmy();
		
		Army deployUnit = mainArmy.battleArmy(enemyArmy,mainPlayer.getPtoePowerRatio());

		if(deployUnit==null){
			System.out.println("Main player has lost");
			return;
		}
		//Subtract the deployed units from main army since the remaining army may need to battle other enemy armies
		mainArmy = mainArmy.subtract(deployUnit);
		System.out.print(mainPlayer.getName()+" must deploy :");
		for(Battalion b:deployUnit.getMap().values()){
			System.out.print(b.getUnits()+" units of "+b.getHierarchy().name+",");
		}
		System.out.println();
	}

	/**
	 * Initalise the game data with the properties and the players . The players consist of the main players and his enemies.
	 */
    public void initGameData()
    {
        // 1.Initialize the properties
        Property.initProperties();
        // 2.Initialize the players and main player army
        this.playerManager.setMainPlayer(new Player("Lengaburu"));
        this.playerManager.getMainPlayer().prepareArmy();// init
         Player p = new Player("Californica");
         this.playerManager.addEnemies(p);
         p.getArmy().addBattalion(new Battalion(150, Property.getTypes().get("Horse")));
         p.getArmy().addBattalion(new Battalion(96, Property.getTypes().get("Elephant")));
         p.getArmy().addBattalion(new Battalion(26, Property.getTypes().get("Tank")));
         p.getArmy().addBattalion(new Battalion(8, Property.getTypes().get("SlingShot")));
         
         
        // this.playerManager.addEnemies(new Player("Britanica"));

    }
    public static void main(String[] args)
    {

        BattleArena game = new BattleArena();
        game.initGameData();


        Comparator<Property> behaviour = new ByHierarchy();
        game.playerManager.getMainPlayer().setArmyBehaviour(behaviour);

       // System.out.println(game.playerManager.getMainPlayer());

        // this will update all the static property of all players horizontal and vertical approach
//        Property p = Property.getTypes().get("Horse");
//        game.propertyManager.updateProperty(p, 5, 10);
//
//        System.out.println("After change");
//        System.out.println(game.playerManager.getMainPlayer());

        
        //main loop
        
        for(Player currentEnemy : game.playerManager.getEnemies()){
        	game.battlePlayers(game.playerManager.getMainPlayer(), currentEnemy);
        }

    }
}
