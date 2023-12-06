const getPlaceByName = (request, h) => {
  const { name } = request.params;

  const place = Places.filter((n) => n.name === name)[0];

  if (name !== undefined) {
    return {
      status: 'success',
      data: {
        place,
      },
    };
  }
  const response = h.response({
    status: 'fail',
    message: 'Tempat tidak ditemukan',
  });
  response.code(404);
  return response;
};


const getPlacesByPrice = (category) => {
  let minPrice, maxPrice;
  
  switch (category) {
    case '$':
      minPrice = 0;
      maxPrice = 100000;
      break;
    case '$$':
      minPrice = 100001;
      maxPrice = 300000;
      break;
    case '$$$':
      minPrice = 300001;
      maxPrice = Infinity; 
      break;
    default:
      return 'Kategori harga tidak valid';
  }

  const filteredPlaces = places.filter(place => place.price >= minPrice && place.price <= maxPrice);

  return filteredPlaces;
};

const getPlaceByPrice = (request, h) => {
  const { category } = request.params;

  const placesInCategory = getPlacesByPrice(category);

  if (Array.isArray(placesInCategory) && placesInCategory.length > 0) {
    return {
      status: 'success',
      data: {
        places: placesInCategory,
      },
    };
  } else {
    const response = h.response({
      status: 'fail',
      message: 'Tidak ada tempat dalam rentang harga tersebut',
    });
    response.code(404);
    return response;
  }
};

const getPlacesByRating = (rating) => {
  const filteredPlaces = places.filter(place => place.rating >= rating);

  return filteredPlaces;
};

const getPlaceByRating = (request, h) => {
  const { rating } = request.params;

  const placesWithRating = getPlacesByRating(parseInt(rating));

  if (Array.isArray(placesWithRating) && placesWithRating.length > 0) {
    return {
      status: 'success',
      data: {
        places: placesWithRating,
      },
    };
  } else {
    const response = h.response({
      status: 'fail',
      message: 'Tidak ada tempat dengan rating yang diminta',
    });
    response.code(404);
    return response;
  }
};