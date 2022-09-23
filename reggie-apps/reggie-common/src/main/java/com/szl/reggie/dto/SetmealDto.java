package com.szl.reggie.dto;
import com.szl.reggie.entity.Setmeal;
import com.szl.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
