package ge.tsu.spring.lecture3.car.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.tsu.spring.lecture3.car.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("carFileImpl")
public class CarServiceFileImpl implements CarService {

  private static final String JSON_DATA = "src/main/resources/cars.json";
  private  static final List<CarView> carViews = new ArrayList<>();
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final File file=new File(JSON_DATA);

  private  void AddToFile(){
    try {
      if (file.exists() && file.isFile())
      {
        file.delete();
      }
      file.createNewFile();
      objectMapper.writeValue(file, carViews);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void add(AddCar addCar) throws RecordAlreadyExistsException{

    Optional<CarView> exists = carViews
            .stream()
            .filter(it -> addCar.getManufacturer().equals(it.getManufacturer()) && it.getModel().equals(addCar.getModel()))
            .findFirst();
    if (exists.isPresent()) {
      throw new RecordAlreadyExistsException(
              String.format("Car with %s and %s already exists", addCar.getManufacturer(), addCar.getModel()));
    }
    carViews.add(new CarView(
            UUID.randomUUID().toString(),
            addCar.getManufacturer(),
            addCar.getModel(),
            addCar.getSpeed()
    ));
    AddToFile();
  }

  @Override
  public void update(String id, AddCar addCar) throws RecordAlreadyExistsException, RecordNotFoundException {
    for (CarView carView : carViews) {
      if (carView.getId().equals(id)) {
        carView.setManufacturer(addCar.getManufacturer());
        carView.setModel(addCar.getModel());
        carView.setSpeed(addCar.getSpeed());
        AddToFile();
      }
    }
    throw new RecordNotFoundException("Unable to find car with specified id");
  }

  @Override
  public List<CarView> getList(String manufacturer, String model) {
    if (manufacturer != null && model != null) {
      return carViews
              .stream()
              .filter(it -> it.getManufacturer().contains(manufacturer) && it.getModel().contains(model))
              .collect(Collectors.toList());
    }
    return carViews;
  }

  @Override
  public CarView getById(String id) throws RecordNotFoundException {
    for (CarView carView : carViews) {
      if (carView.getId().equals(id)) {
        return carView;
      }
    }
    throw new RecordNotFoundException("Unable to find car with specified id");
  }

  @Override
  public void delete(String id) throws RecordNotFoundException {
    Iterator<CarView> carViewIterator = carViews.iterator();
    while (carViewIterator.hasNext()) {
      CarView carView = carViewIterator.next();
      if (carView.getId().equals(id)) {
        carViewIterator.remove();
        AddToFile();
        return;
      }
    }

    throw new RecordNotFoundException("Unable to find car with specified id");
  }
}
