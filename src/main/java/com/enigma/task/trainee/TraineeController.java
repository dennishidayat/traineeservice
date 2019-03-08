package com.enigma.task.trainee;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.task.trainee.dao.TraineeDao;
import com.enigma.task.trainee.dto.CommonResponse;
import com.enigma.task.trainee.dto.TraineeDto;
import com.enigma.task.trainee.exception.CustomException;
import com.enigma.task.trainee.model.Trainee;

@RestController
@RequestMapping("/trainee")
@SuppressWarnings("rawtypes")
public class TraineeController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TraineeDao traineeDao;
	
	@GetMapping(value="/{trainee}")
	public CommonResponse<TraineeDto> getById(@PathVariable("trainee") String traineeId) throws CustomException {
		try {
			Trainee trainee = traineeDao.getById(Integer.parseInt(traineeId));
			
			return new CommonResponse<TraineeDto>(modelMapper.map(trainee, TraineeDto.class));
		} catch (CustomException e) {
			return new CommonResponse<TraineeDto>("01", e.getMessage());
		} catch (NumberFormatException e) {
			return new CommonResponse<TraineeDto>("06", "input must be a number");
		} catch (Exception e) {
			return new CommonResponse<TraineeDto>("06", e.getMessage());
		}
	}

	@PostMapping(value="")
	public CommonResponse<TraineeDto> insert(@RequestBody TraineeDto traineeDto) throws CustomException {
		try {
			Trainee trainee =  modelMapper.map(traineeDto, Trainee.class);
			trainee.setTraineeId(0);
			trainee =  traineeDao.save(trainee);
			
			return new CommonResponse<TraineeDto>(modelMapper.map(trainee, TraineeDto.class));
			} catch (CustomException e) {
				return new CommonResponse<TraineeDto>("14", "trainee not found");
			} catch (NumberFormatException e) {
				return new CommonResponse<TraineeDto>("200", "adsfads");
			} catch (Exception e) {
				return new CommonResponse<TraineeDto>("404", "adsfadsf");
			}
		}
	
	@PutMapping(value="")
	public CommonResponse<TraineeDto> update(@RequestBody TraineeDto traineeDto) throws CustomException {
		try {
			Trainee checkTrainee = traineeDao.getById(traineeDto.getTraineeId());
			if (traineeDto.getTraineeId() == null) {
				return new CommonResponse<TraineeDto>("14", "trainee not found");
			}
			if (traineeDto.getBootcampBatch() != null) {
				checkTrainee.setBootcampBatch(traineeDto.getBootcampBatch());
			}
			if (traineeDto.getFirstName() != null) {
				checkTrainee.setFirstName(traineeDto.getFirstName());
			}
			if (traineeDto.getLastName() != null) {
				checkTrainee.setLastName(traineeDto.getLastName());
			}
			if (traineeDto.getAddress() != null) {
				checkTrainee.setAddress(traineeDto.getAddress());
			}
			if (traineeDto.getEmail() != null) {
				checkTrainee.setEmail(traineeDto.getEmail());
			}
			if (traineeDto.getPhone() != null) {
				checkTrainee.setPhone(traineeDto.getPhone());
			}
			if (traineeDto.getActiveFlag() != null) {
				checkTrainee.setActiveFlag(traineeDto.getActiveFlag());
			}
			
			checkTrainee = traineeDao.save(checkTrainee);
			
			return new CommonResponse<TraineeDto>(modelMapper.map(checkTrainee, TraineeDto.class));
		} catch (CustomException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@DeleteMapping(value="/{traineeId}")
	public CommonResponse<TraineeDto> delete(@PathVariable("traineeId") String traineeId) throws CustomException {
		try {
			Trainee Checktrainee = traineeDao.getById(Integer.parseInt(traineeId));
			if (Checktrainee == null) {
				return new CommonResponse("06", "trainee not found");
			}
			traineeDao.delete(Checktrainee);
			return new CommonResponse();
		} catch (CustomException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@GetMapping(value="")
	public CommonResponse getList(@RequestParam(name="list", defaultValue="") String traineeTemp) throws CustomException {
		
		try {
			List<Trainee> trainee = traineeDao.getList();
			
			return new CommonResponse<List<TraineeDto>>(trainee.stream()
//					.filter(trn -> "bambang".equals(trn.getFirstName()))
					.map(trn -> modelMapper.map(trn, TraineeDto.class))
					.collect(Collectors.toList())
					);
		
		} catch (CustomException e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@GetMapping(value="/active")
	public CommonResponse getListByActiveFlag(@RequestParam(name="list", defaultValue="") String traineeTemp) throws CustomException {
		
		try {
			List<Trainee> trainee = traineeDao.getListByActiveFlag();
			
			return new CommonResponse<List<TraineeDto>>(trainee.stream()
					.map(trn -> modelMapper.map(trn, TraineeDto.class))
					.collect(Collectors.toList()));
		
		} catch (CustomException e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}

}
