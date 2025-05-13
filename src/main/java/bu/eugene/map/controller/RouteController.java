package bu.eugene.map.controller;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.dto.RouteDto;
import bu.eugene.map.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/new")
    public ResponseEntity<?> createRoute(@RequestHeader("Authorization") String token, @RequestBody RouteDto routeDto) {
        routeService.crateRoute(routeDto, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public Page<RouteDto> getRoutes(Pageable pageable) {
        return routeService.getRoutePage(pageable);
    }

    @GetMapping("/all/person")
    public Page<RouteDto> findByPerson(@RequestHeader("Authorization") String token, Pageable pageable) {
        return routeService.findAllByPerson(token, pageable);
    }
}
