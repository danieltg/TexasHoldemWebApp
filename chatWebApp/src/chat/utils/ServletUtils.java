package chat.utils;

import Engine.Lobby;
import Engine.chat.ChatManager;
import Engine.users.UserManager;
import Engine.GamesDescriptorManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
	private static final String LOBBY_ATTRIBUTE_NAME = "lobbyManager";
	private static final String GAME_DESCRIPTOR_MANAGER_ATTRIBUTE_NAME = "gamesDescriptorManager";


	public static Lobby getLobby(ServletContext servletContext) {
		if (servletContext.getAttribute(LOBBY_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(LOBBY_ATTRIBUTE_NAME, new Lobby());
		}
		return (Lobby) servletContext.getAttribute(LOBBY_ATTRIBUTE_NAME);
	}


	public static UserManager getUserManager(ServletContext servletContext) {
		if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static GamesDescriptorManager getGamesDescriptorManager(ServletContext servletContext) {
		if (servletContext.getAttribute(GAME_DESCRIPTOR_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(GAME_DESCRIPTOR_MANAGER_ATTRIBUTE_NAME, new GamesDescriptorManager());
		}
		return (GamesDescriptorManager) servletContext.getAttribute(GAME_DESCRIPTOR_MANAGER_ATTRIBUTE_NAME);
	}

	public static ChatManager getChatManager(ServletContext servletContext) {
		if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}
