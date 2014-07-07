package org.human.gulim.runcatch.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.human.gulim.runcatch.bean.Jsonable;
import org.human.gulim.runcatch.bean.RoomInfo;
import org.human.gulim.runcatch.contants.Constants;
import org.human.gulim.runcatch.exception.NetworkMethodException;
import org.json.simple.JSONObject;


public class NetworkMethodJavaSocketImpl implements NetworkMethod {

	@Override
	public RoomInfo emitEvent(String event, Jsonable data) throws NetworkMethodException {
		Socket socket = null;
		ObjectOutputStream oOut = null;
		ObjectInputStream oIn = null;
		RoomInfo result = null;
		Object request, response;
		try {
			socket = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
			oOut = new ObjectOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));

			request = toRequest(event,data);
			
			oOut.writeObject(request);
			oOut.flush();

			oIn = new ObjectInputStream(new BufferedInputStream(
					socket.getInputStream()));
			response = oIn.readObject();
			result = responseToRoomInfo(response);

		} catch (UnknownHostException e) {
			throw new NetworkMethodException(e);
		} catch (IOException e) {
			throw new NetworkMethodException(e);
		} catch (ClassNotFoundException e) {
			throw new NetworkMethodException(e);
		} finally {
			freeResources(socket, oIn, oOut);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Object toRequest(String event, Jsonable jsonable) {
		JSONObject obj = new JSONObject();
		obj.put("event", event);
		obj.put("data", jsonable);
		
		return obj.toJSONString();
	}

	private RoomInfo responseToRoomInfo(Object response) {
		RoomInfo roomInfo = null;
		JSONObject obj = null;
		obj = (JSONObject) response;
		
		roomInfo = RoomInfo.getRoomInfoFromJson((JSONObject)obj.get("data"));
		return roomInfo;
	}

	
	/**
	 * It frees socket, inputstream, outputstream.
	 * @param socket to be freed.
	 * @param in to be freed.
	 * @param out to be freed.
	 */
	private void freeResources(Socket socket, InputStream in, OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	
}
