package bu.eugene.map.controller;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@CrossOrigin("*")
public class PlaceController {

    private final PlaceService placeService;

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/place")
    public ResponseEntity<Void> handleAccountOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "Authorization")
                .build();
    }

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
