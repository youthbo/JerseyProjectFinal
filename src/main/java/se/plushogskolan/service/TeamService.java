package se.plushogskolan.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import se.plushogskolan.service.ServiceException;
import se.plushogskolan.model.Status;
import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.repository.TeamRepository;
import se.plushogskolan.repository.UserRepository;

@Service
public class TeamService {
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private UserRepository userRepository;

	public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	// used in testConfig file, for autowired in test
	public TeamService() {
	}

	@Transactional
	public Team createTeam(Team team) {
		try {
			if (team.getId() == null) {
				team.setStatus(Status.ACTIVE.toString());
				return teamRepository.save(team);

			} else {
				throw new ServiceException("Team with this teamname already exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not add team: " + team.getName(), e);
		}
	}

	public Iterable<Team> findAllTeams() {
		return teamRepository.findAll();
	}

	public Team findByName(String teamName) {
		Team team = teamRepository.findByName(teamName);
		return team;
	}

	public Team findOne(Long id) {
		Team team = teamRepository.findOne(id);
		return team;
	}

	@Transactional
	public void uppdateTeam(Long id, String newName) {
		try {
			Team team = teamRepository.findOne(id);

			Team newTeam = teamRepository.findByName(newName);
			if (newTeam == null) {
				team.setName(newName);
				teamRepository.save(team);
			} else {
				throw new ServiceException("Team with this teamname " + newName + " exists");

			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team", e);
		}
	}

	@Transactional
	public void updateStatusTeam(Long id, Status status) {

		try {
			Team team = teamRepository.findOne(id);
			if (team != null) {
				team.setStatus(status.toString());
				teamRepository.save(team);
			} else {
				throw new ServiceException("Team with this teamname NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not deactivate team with id: " + id, e);
		}

	}

	@Transactional
	public void assigneUserToTeam(Long teamId, Long userId) {
		try {
			Team team = teamRepository.findOne(teamId);
			User user = userRepository.findOne(userId);
			if ((team != null) && (user != null)) {
				List<User> users = userRepository.findAllByTeam(team);
				if (users.size() < 10) {
					user.setTeam(team);
					userRepository.save(user);

				} else {
					throw new ServiceException(
							"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
				}

			} else {
				throw new ServiceException("Team with this teamId NOT exists OR User NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team", e);
		}
	}

}
