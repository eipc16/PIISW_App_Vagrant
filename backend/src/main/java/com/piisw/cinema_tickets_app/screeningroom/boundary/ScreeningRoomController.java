package com.piisw.cinema_tickets_app.screeningroom.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningRoomDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObjectState;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAdminRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.screeningroom.control.ScreeningRoomService;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "Screening Rooms")
@RestController
@RequestMapping(ScreeningRoomController.MAIN_PATH)
public class ScreeningRoomController {

    public static final String MAIN_PATH = "/screening-rooms";
    private static final String IDS = "ids";
    public static final String IDS_PATH = "/{" + IDS + "}";
    private static final String STATE = "objectState";

    @Autowired
    private ScreeningRoomService screeningRoomService;

    @Autowired
    private ScreeningRoomMapper screeningRoomMapper;

    @ApiOperation(value = "${api.screening.room.get.value}", notes = "${api.screening.room.get.notes}")
    @GetMapping(IDS_PATH)
    @HasAnyRole
    public List<ScreeningRoomDTO> getScreeningRoomsByIds(@ApiParam(value = "${api.screening.room.ids}") @PathVariable(IDS) Set<Long> ids,
            @ApiParam(value = "${api.screening.room.states}") @RequestParam(name = STATE, defaultValue = "ACTIVE") Set<AuditedObjectState> objectStates) {
        return screeningRoomService.getAllScreeningRoomsByIdsAndObjectStates(ids, objectStates).stream()
                .map(screeningRoomMapper::mapToScreeningRoomDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "${api.screening.room.post.value}", notes = "${api.screening.room.post.notes}")
    @PostMapping
    @HasAdminRole
    public ResourceDTO createScreeningRoom(@RequestBody @Validated ScreeningRoomDTO screeningRoomDTO) {
        ScreeningRoom screeningRoom = screeningRoomMapper.mapToScreeningRoom(screeningRoomDTO);
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(screeningRoom);
        return screeningRoomMapper.mapScreeningRoomToResourceDTO(createdScreeningRoom);
    }

    @ApiOperation(value = "${api.screening.room.put.value}", notes = "${api.screening.room.put.notes}")
    @PutMapping
    @HasAdminRole
    public ResourceDTO updateScreeningRoom(@Validated @RequestBody ScreeningRoomDTO screeningRoomDTO) {
        ScreeningRoom screeningRoom = screeningRoomMapper.mapToScreeningRoom(screeningRoomDTO);
        ScreeningRoom updatedScreeningRoom = screeningRoomService.updateScreeningRoom(screeningRoom);
        return screeningRoomMapper.mapScreeningRoomToResourceDTO(updatedScreeningRoom);
    }

    @ApiOperation(value = "${api.screening.room.delete.value}", notes = "${api.screening.room.delete.notes}")
    @DeleteMapping(IDS_PATH)
    @HasAdminRole
    public List<ResourceDTO> deleteScreeningRoomByIds(@ApiParam(value = "${api.screening.room.rm.ids}") @PathVariable(IDS) Set<Long> ids) {
        return screeningRoomService.deleteScreeningRoomsByIds(ids).stream()
                .map(screeningRoomMapper::mapScreeningRoomToResourceDTO)
                .collect(Collectors.toList());
    }

}
