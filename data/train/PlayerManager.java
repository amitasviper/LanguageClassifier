package in.problem.handler;

import in.problem.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager
{
    private Player mainPlayer = null;

    private List<Player> enemies;

    public List<Player> getEnemies()
    {
        return enemies;
    }

    public PlayerManager()
    {
        enemies = new ArrayList<Player>();
    }

    public Player getMainPlayer()
    {
        return mainPlayer;
    }

    public void setMainPlayer(Player mainPlayer)
    {
        this.mainPlayer = mainPlayer;
    }

    public void addEnemies(Player p)
    {
        if (p != null)
            enemies.add(p);
    }

    public List<Player> getAllPlayers()
    {

        List<Player> list = new ArrayList<Player>(enemies);
        list.add(mainPlayer);
        return list;
    }

}
