package bu.eugene.map.controller;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@CrossOrigin("http://localhost:8080")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/find")
    public List<PlaceDto> findByCityName(@RequestParam String cityName) {
        return placeService.getAllPlacesInfoByAPI(cityName);
    }

    @PostMapping("/")
    public PlaceDto findOrCreate(@RequestBody PlaceDto placeDto) {
        return placeService.findOrCreate(placeDto);
    }
}
