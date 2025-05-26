package bu.eugene.map.controller;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/all")
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/allWithImages")
    public List<PlaceDto> getAllPlacesWithImages() {
        return placeService.getAllPlacesWithImages();
    }

    @GetMapping("/findByParam")
    public PlaceDto findPlacesByParam(@RequestParam("value") String param) {
        return placeService.findByParam(param);
    }
}
