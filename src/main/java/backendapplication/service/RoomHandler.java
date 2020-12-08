package backendapplication.service;

import dto.RoomDTO;
import dto.VacantRoomsDTO;
import service.RoomUtility;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RoomHandler implements RoomUtility {
    @Override
    public Collection<RoomDTO> findVacantRooms(VacantRoomsDTO vacantRoomsDTO) {
        return null;
    }

    @Override
    public boolean markRoomAsReserved(List<String> list) {
        return false;
    }
}
