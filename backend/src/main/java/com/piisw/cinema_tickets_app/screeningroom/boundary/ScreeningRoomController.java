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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "Screening Rooms")
@RestController
@RequestMapping(ScreeningRoomController.MAIN_PATH)
public class ScreeningRoomController {

    public static final String MAIN_PATH = "/screening-rooms";
    private static final String IDS = "ids";
    private static final String IDS_PATH = "/{" + IDS + "}";
    private static final String STATE = "objectState";

    @Autowired
    private ScreeningRoomService screeningRoomService;

    @Autowired
    private ScreeningRoomMapper screeningRoomMapper;

    @ApiOperation(value = "Return screening rooms by ids", notes = "Returns screening rooms with supplied ids. "
            + "Additionally allowed object states for queried screening rooms may by specified.")
    @GetMapping(IDS_PATH)
    @HasAnyRole
    public List<ScreeningRoomDTO> getScreeningRoomsByIds(@PathVariable(IDS) Set<Long> ids,
                                                         @RequestParam(name = STATE, required = false) Set<AuditedObjectState> objectStates) {
        return screeningRoomService.getAllScreeningRoomsByIdsAndObjectStates(ids, objectStates).stream()
                .map(screeningRoomMapper::mapToScreeningRoomDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Create screening room", notes = "Creates screening room based on supplied data.")
    @PostMapping
    @HasAdminRole
    public ResourceDTO createScreeningRoom(@RequestBody @Validated ScreeningRoomDTO screeningRoomDTO) {
        ScreeningRoom screeningRoom = screeningRoomMapper.mapToScreeningRoom(screeningRoomDTO);
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(screeningRoom);
        return ResourceDTO.builder()
                .id(createdScreeningRoom.getId())
                .identifier(screeningRoom.getNumber().toString())
                .uri(buildScreeningRoomUri(Set.of(createdScreeningRoom.getId())))
                .build();
    }

    private URI buildScreeningRoomUri(Set<Long> ids) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MAIN_PATH)
                .path(IDS_PATH)
                .buildAndExpand(ids)
                .toUri();
    }

}
