package com.techguru.trading.controller;

import com.techguru.trading.model.SymbolType;
import com.techguru.trading.service.SymbolTypeService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/symboltype")
public class SymbolTypeController {

  private SymbolTypeService symbolTypeService;

  @Autowired
  public SymbolTypeController(SymbolTypeService symbolTypeService) {
    this.symbolTypeService = symbolTypeService;
  }

  @ApiOperation(value = "Add a symbol type", notes = "This endpoint creates a new symbol type")
  @PostMapping
  public ResponseEntity<SymbolType> addSymbolType(@RequestBody SymbolType symbolType) {
    return new ResponseEntity<>(symbolTypeService.addSymbolType(symbolType), HttpStatus.OK);
  }

  @ApiOperation(value = "Find all symbol types", notes = "Find all symbol types")
  @GetMapping
  public ResponseEntity<List<SymbolType>> findAllSymbolTypes() {
    return new ResponseEntity<>(symbolTypeService.findAllSymbolTypes(), HttpStatus.OK);
  }
}
