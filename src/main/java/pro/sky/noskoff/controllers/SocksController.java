package pro.sky.noskoff.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.noskoff.model.Socks;
import pro.sky.noskoff.model.SocksColor;
import pro.sky.noskoff.model.SocksCottonPart;
import pro.sky.noskoff.model.SocksSize;
import pro.sky.noskoff.services.SocksService;

import java.util.List;

@RestController
@RequestMapping("/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }


    @PostMapping("/add")
    @Operation(summary = "Добавить партию носков на склад", description = "Необходимо указать цвет, процентное содержание хлопка, размер, а также количество пар носков")
    @ApiResponse(responseCode = "200", description = "Партия носков добавлена на склад", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Socks.class)))
    })
    public ResponseEntity<Socks> addSocks(@RequestParam SocksColor color, @RequestParam SocksCottonPart cotton, @RequestParam SocksSize size, @RequestParam int quantity) {
        Socks createSocks = socksService.addSocks(new Socks(color, cotton, size, quantity));
        return ResponseEntity.ok(createSocks);
    }

    @PutMapping
    @Operation(summary = "Отпустить партию носков со склада", description = "Необходимо указать цвет, процентное содержание хлопка, размер, а также количество пар носков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Партия отпущена"),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")

    })
    public ResponseEntity<Void> putSocks(@RequestParam SocksColor color, @RequestParam SocksCottonPart cotton, @RequestParam SocksSize size, @RequestParam int quantity) {
        if (socksService.deleteSocks(color, size, cotton, quantity)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAll")
    @Operation(summary = "Получить все имеющиеся на складе носки")
    @ApiResponse(responseCode = "200", description = "Вывод всех носков", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Socks.class)))
    })
    public ResponseEntity<List<Socks>> getAllSocks() {
        List<Socks> allSocks = socksService.getAllSocks();
        if (allSocks.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allSocks);
    }

    @DeleteMapping
    @Operation(summary = "Списать испорченные (бракованные) носки", description = "Необходимо указать цвет, процентное содержание хлопка, размер, а также количество пар носков для списания")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Партия списана", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Socks.class)))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")

    })
    public ResponseEntity<Void> deleteSocks(@RequestParam SocksColor color, @RequestParam SocksCottonPart cotton, @RequestParam SocksSize size, @RequestParam int quantity) {
        if (socksService.deleteSocks(color, size, cotton, quantity)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    @Operation(summary = "Получить информацию по наличию на складе", description = "Введите параметры, по которому производить отбор носков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Партия списана"),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")

    })
    public ResponseEntity<String> getCountSocksByParameters(@RequestParam SocksColor color, @RequestParam SocksSize size, @RequestParam SocksCottonPart cottonMin, @RequestParam SocksCottonPart cottonMax) {
        String count = socksService.getCountSocksByParameters(color, size, cottonMin, cottonMax);
        return ResponseEntity.ok(count);
    }


}
