package com.brazcubas.apsii.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brazcubas.apsii.model.ApiDtos;
import com.brazcubas.apsii.service.ModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoints do catálogo de modelos preditivos.
 */
@RestController
@RequestMapping("/api/v1/models")
@Tag(name = "Models")
public class ModelController {
    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @Operation(summary = "List models")
    @GetMapping
    public ApiDtos.ModelsResponse list() {
        return modelService.listActive();
    }

    @Operation(
            summary = "Get model",
            description = "Retorna os detalhes de um modelo preditivo do catálogo a partir do "
                    + "seu identificador. A consulta por identificador não filtra pela situação "
                    + "do modelo: um modelo inativo também é retornado, com o campo `active` "
                    + "igual a `false` (a listagem em GET /api/v1/models devolve apenas "
                    + "modelos ativos).")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Modelo encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiDtos.ModelDetail.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Modelo não encontrado. "
                            + "O corpo traz o código de erro `MODEL_NOT_FOUND`.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiDtos.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "MODEL_NOT_FOUND",
                                    value = "{\"error\":{\"code\":\"MODEL_NOT_FOUND\","
                                            + "\"message\":\"The requested prediction model "
                                            + "was not found.\"}}")))
    })
    @GetMapping("/{model_id}")
    public ApiDtos.ModelDetail get(
            @Parameter(
                    name = "model_id",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Identificador único do modelo no catálogo (ex.: `model-a`). "
                            + "Diferencia maiúsculas de minúsculas.",
                    example = "model-a")
            @PathVariable("model_id") String modelId) {
        return modelService.get(modelId);
    }
}