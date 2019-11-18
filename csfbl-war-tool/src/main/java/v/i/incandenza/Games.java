package v.i.incandenza;

public class Games {
	private Team playerTeam;
	private int games;
	
	public Team getTeam() {
		return this.playerTeam;
	}
	
	public int getGames() {
		return this.games;
	}
	
	public Games(String games, Team team) {
		this.games = Integer.parseInt(games);
		this.playerTeam = team;
	}
}
