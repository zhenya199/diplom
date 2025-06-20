package bu.eugene.map.controller;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class PlaceController {

    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);
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
        System.out.println(cityName);
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
    public Page<PlaceDto> findPlacesByParam(@RequestParam("value") String param, Pageable pageable) {
        log.info("info: " + param);
        return placeService.findByParam(param, pageable);
    }
}
