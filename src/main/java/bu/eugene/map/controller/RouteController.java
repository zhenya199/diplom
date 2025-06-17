package bu.eugene.map.controller;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.dto.RouteDto;
import bu.eugene.map.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/new")
    public ResponseEntity<?> createRoute(@RequestHeader("Authorization") String token, @RequestBody RouteDto routeDto) {
        routeService.crateRoute(routeDto, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/image/{id}")
    public RouteDto addImageToRoute(@PathVariable("id") Integer routeId,
                                    @ModelAttribute ImageDto imageDto) {
        return routeService.addImage(routeId, imageDto);
    }

    @GetMapping("/all")
    public Page<RouteDto> getRoutes(Pageable pageable) {
        return routeService.getRoutePage(pageable);
    }

    @GetMapping("/{id}")
    public RouteDto getRoute(@PathVariable Integer id) {
        return routeService.findRouteDtoById(id);
    }

    @GetMapping("/all/person")
    public List<RouteDto> findByPerson(@RequestHeader("Authorization") String token, Pageable pageable) {
        return routeService.findAllByPerson(token, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@RequestHeader("Authorization") String token,
                                         @PathVariable("id") Integer id) {
        routeService.deleteRoute(token, id);
        return ResponseEntity.ok().build();
    }
}
