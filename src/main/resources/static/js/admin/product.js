$(document).ready(function() {

	$("#pro-detail-type label").hide();
	loadProductList();
	// 라디오 버튼이 변경될 때마다 실행
	$('#pet-type input[type="radio"], #pro-type input[type="radio"]').change(function() {



		// 선택된 petType과 proType 값 확인
		var petType = $('input[name="pet-filter"]:checked').val();
		var proType = $('input[name="pro-filter"]:checked').val();

		// 상세타입 초기화
		$("#pro-detail-type label").hide();
		$("#pro-detail-type input[type='radio']").prop('checked', false); // 체크 해제
		// 검색 필드 초기화
		$('#search-filter').val('');

		if (petType === "강아지") {
			if (proType === "사료") {
				$('input[name="type-DF"]').parent().show();
				$('input[name="type-DF"]').first().prop('checked', true); // 첫 번째 항목 체크
			} else if (proType === "간식") {
				$('input[name="type-DS"]').parent().show();
				$('input[name="type-DS"]').first().prop('checked', true); // 첫 번째 항목 체크
			} else if (proType === "용품") {
				$('input[name="type-DG"]').parent().show();
				$('input[name="type-DG"]').first().prop('checked', true); // 첫 번째 항목 체크
			}
		} else if (petType === "고양이") {
			if (proType === "사료") {
				$('input[name="type-CF"]').parent().show();
				$('input[name="type-CF"]').first().prop('checked', true); // 첫 번째 항목 체크
			} else if (proType === "간식") {
				$('input[name="type-CS"]').parent().show();
				$('input[name="type-CS"]').first().prop('checked', true); // 첫 번째 항목 체크
			} else if (proType === "용품") {
				$('input[name="type-CG"]').parent().show();
				$('input[name="type-CG"]').first().prop('checked', true); // 첫 번째 항목 체크
			}
		}

	});


	$('#pet-type input[type="radio"], #pro-type input[type="radio"], #pro-detail-type input[type="radio"]').change(function() {
		loadProductList();
	});

	// 검색 버튼 클릭 시 loadProductList 호출
	$('#searchBtn').click(function() {
		loadProductList();
	});



	// 쿠폰 등록 탭 데이터 로드 함수
	function loadProductList() {
		const itemsPerPage = 10; // 페이지 당 item 수
		let currentPage = 1;
		let totalItems = 0;
		let couponList = []; // 데이터 저장할 배열
		let currPageGroup = 1;
		let totalPages = 0;

		let petType = $('input[name="pet-filter"]:checked').val();
		let proType = $('input[name="pro-filter"]:checked').val();
		let detailType = $('input[name="type-DF"]:checked, input[name="type-DS"]:checked, input[name="type-DG"]:checked, input[name="type-CF"]:checked, input[name="type-CS"]:checked, input[name="type-CG"]:checked').val() || null;
		let searchType = $('#search-filter').val() || null;



		fetchData(currentPage, currPageGroup);

		function fetchData(currentPage, currPageGroup) {
			$.ajax({
				url: '/admin/product/list',
				method: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					petType,
					proType,
					detailType,
					searchType
				}),
				success: function(products) {
					productsList = products;
					totalItems = productsList.length;
					totalPages = Math.ceil(totalItems / itemsPerPage);

					displayItems(currentPage);
					setupPagination(currentPage, currPageGroup);
				},
				error: function(xhr, status, error) {
					console.error('Error fetching data:', error);
				}
			});
		}

		function displayItems(currentPage) {
			const start = (currentPage - 1) * itemsPerPage;
			const end = start + itemsPerPage;
			const sliceList = productsList.slice(start, end);

			let lists = '';
			$.each(sliceList, function(index, pro) {

				const date = new Date(pro.pro_date); // Date 객체로 변환
				const formattedDate = date.toISOString().split('T')[0]; // 'YYYY-MM-DD' 형식으로 변환

				lists += '<tr>';
				lists += '<td>' + formattedDate + '</td>';
				lists += '<td>' + pro.pro_code + '</td>';
				lists += '<td>' + pro.pro_name + '</td>';
				lists += '<td>' + pro.pro_pets + ' / ' + pro.pro_type + ' / ' + pro.pro_category + '</td>';
				lists += '<td>' + pro.pro_onoff + '</td>';
				lists += `<td>
				                  <button type="button" id="modify-btn" class="btn-style" data-product-code="${pro.pro_code}">상세보기</button>
				              </td>`;
				lists += '</tr>';
			});

			$('#product-table-body').html(lists);

		}

		function setupPagination(currentPage, currPageGroup) {
			const maxPagesToShow = 10;
			const startPage = (currPageGroup - 1) * maxPagesToShow + 1;
			const endPage = Math.min(startPage + maxPagesToShow - 1, totalPages);

			let paginationHtml = '';
			if (currPageGroup > 1) {
				paginationHtml += '<a href="#" class="page-link" data-page="prev-group">&laquo; 이전</a>';
			}
			for (let i = startPage; i <= endPage; i++) {
				paginationHtml += '<a href="#" class="page-link ' + (i === currentPage ? 'active' : '') + '" data-page="' + i + '">' + i + '</a>';
			}
			if (endPage < totalPages) {
				paginationHtml += '<a href="#" class="page-link" data-page="next-group">다음 &raquo;</a>';
			}

			$('#pagination').html(paginationHtml);

			$('.page-link').on('click', function(event) {
				event.preventDefault();

				let clickedPage = $(this).data('page');
				if (clickedPage === 'prev-group') {
					currPageGroup--;
					currentPage = (currPageGroup - 1) * maxPagesToShow + 1;
				} else if (clickedPage === 'next-group') {
					currPageGroup++;
					currentPage = (currPageGroup - 1) * maxPagesToShow + 1;
				} else {
					currentPage = clickedPage;
				}

				displayItems(currentPage);
				setupPagination(currentPage, currPageGroup);
			});
		}
	};


	// 신규등록 모달 열기/닫기
	$('#new-pro-btn').on('click', function() {
		// 기존 미리보기 이미지 초기화
		document.getElementById('mainImagePreview').innerHTML = ''; // 대표 이미지 미리보기 초기화
		document.getElementById('desImagePreview').innerHTML = '';  // 상세 이미지 미리보기 초기화

		// 선택된 파일 배열 초기화
		mainSelectedFiles = []; // 대표이미지 파일 배열 초기화
		desSelectedFiles = [];  // 상세이미지 파일 배열 초기화


		//resetModal();
		$('#registerProductBtn').text('등록하기');
		document.getElementById('productModal').style.display = 'block';
		document.getElementById('productModalPg').reset();
	});


	$('.close-btn').on('click', function() {
		document.getElementById('productModal').style.display = 'none'; // 모달을 숨김
	});


	let mainSelectedFiles = [];  // 대표이미지 파일 배열
	let desSelectedFiles = [];   // 상세이미지 파일 배열

	// 미리보기 생성 함수
	function handleFileSelect(event, maxFiles, selectedFiles, previewContainerId) {
		const files = Array.from(event.target.files);
		const previewContainer = document.getElementById(previewContainerId);

		// 파일 수 제한 검사
		if (selectedFiles.length + files.length > maxFiles) {
			alert(`이미지는 최대 ${maxFiles}장까지만 업로드할 수 있습니다.`);
			return;
		}

		// 새로 선택된 파일들을 미리보기로 추가하고, 배열에 저장
		files.forEach(file => {
			selectedFiles.push(file);  // 배열에 파일 추가

			const reader = new FileReader();
			reader.onload = function(e) {
				const imgContainer = document.createElement('div');
				imgContainer.style.position = 'relative';
				imgContainer.style.display = 'inline-block';
				imgContainer.style.marginLeft = '10px'; // 간격 추가

				const img = document.createElement('img');
				img.src = e.target.result;
				img.style.width = '60px';
				img.style.height = '60px';
				img.style.objectFit = 'cover';
				img.style.borderRadius = '8px'; // 이미지 모서리 둥글게 설정

				const deleteButton = document.createElement('span');
				deleteButton.innerText = '×';
				deleteButton.style.position = 'absolute';
				deleteButton.style.top = '-3px';
				deleteButton.style.right = '-3px';
				deleteButton.style.width = '15px';
				deleteButton.style.height = '15px';
				deleteButton.style.borderRadius = '50%';
				deleteButton.style.backgroundColor = 'white';
				deleteButton.style.color = 'black';
				deleteButton.style.fontSize = '16px';
				deleteButton.style.display = 'flex';
				deleteButton.style.alignItems = 'center';
				deleteButton.style.justifyContent = 'center';
				deleteButton.style.cursor = 'pointer';
				deleteButton.style.boxShadow = '0 0 3px rgba(0, 0, 0, 0.3)'; // 약간의 그림자 추가
				deleteButton.onclick = () => {
					imgContainer.remove();
					selectedFiles.splice(selectedFiles.indexOf(file), 1);  // 배열에서 파일 제거
				};

				imgContainer.appendChild(img);
				imgContainer.appendChild(deleteButton);
				previewContainer.appendChild(imgContainer);
			};
			reader.readAsDataURL(file);
		});

		// 파일 입력 초기화
		event.target.value = '';
	}

	// 대표이미지 이벤트 핸들러
	document.getElementById('proMainImages').addEventListener('change', function(event) {
		handleFileSelect(event, 5, mainSelectedFiles, 'mainImagePreview');
	});

	// 상세이미지 이벤트 핸들러
	document.getElementById('proDesImages').addEventListener('change', function(event) {
		handleFileSelect(event, 10, desSelectedFiles, 'desImagePreview');
	});





	//상품옵션 +/-

	const optionContainer = document.getElementById('option-container');

	optionContainer.addEventListener('click', function(event) {
		if (event.target.classList.contains('add-option')) {
			addOptionLine();
		} else if (event.target.classList.contains('remove-option')) {
			removeOptionLine(event.target);
		}
	});

	function addOptionLine() {
		const optionGroup = document.querySelector('.input-group2');
		const newOptionGroup = optionGroup.cloneNode(true);

		newOptionGroup.querySelectorAll('input').forEach(input => input.value = "");

		const removeButton = newOptionGroup.querySelector('.remove-option');
		removeButton.style.display = 'inline-block'; // 새 줄의 삭제 버튼 보이기

		optionContainer.appendChild(newOptionGroup);
		newOptionGroup.querySelector('label').innerText = ''; // 레이블을 빈칸으로 설정

		updateButtons();
	}

	function removeOptionLine(button) {
		const optionGroup = button.closest('.input-group2');
		if (optionGroup !== optionContainer.firstElementChild) {
			optionContainer.removeChild(optionGroup);
		}
		updateButtons();
	}

	function updateButtons() {
		const optionGroups = optionContainer.querySelectorAll('.input-group2');

		optionGroups.forEach((group, index) => {
			const removeButton = group.querySelector('.remove-option');

			if (index === 0) {
				// 첫 번째 줄의 삭제 버튼은 숨김
				removeButton.style.display = 'none';
			} else {
				removeButton.style.display = 'inline-block'; // 그 외 줄은 삭제 버튼 보이기
			}
		});
	}



	// 옵션 값을 가져오는 함수
	function getOptionValues() {
		const optionGroups = optionContainer.querySelectorAll('.input-group2');
		const options = [];

		optionGroups.forEach(group => {
			const name = group.querySelector('#optName').value;
			const price = group.querySelector('#optPrice').value;
			const count = group.querySelector('#optCnt').value;

			options.push({
				name: name,
				price: price,
				count: count
			});
		});

		console.log(options); // 옵션 값 확인
		return options;
	}

	//상품등록하기 버튼 ajax 데이터 이동
	$('#registerProductBtn').on('click', function() {

		// FormData 객체 생성
		const formData = new FormData();

		// 최종 선택된 대표 이미지 파일을 FormData에 추가
		mainSelectedFiles.forEach(file => {
			formData.append('mainImages', file); // 키값 'mainImages'로 추가
		});

		// 최종 선택된 상세 이미지 파일을 FormData에 추가
		desSelectedFiles.forEach(file => {
			formData.append('desImages', file); // 키값 'desImages'로 추가
		});

		// 각 입력 필드의 값을 가져오기
		let petType = document.getElementById("petType") ? document.getElementById("petType").value : '';
		let proType = document.getElementById("proType") ? document.getElementById("proType").value : '';
		let proDetailType = document.getElementById("proDetailType") ? document.getElementById("proDetailType").value : '';
		let filterType1 = document.getElementById("filterType1") ? document.getElementById("filterType1").value : '';
		let filterType2 = document.getElementById("filterType2") ? document.getElementById("filterType2").value : '';
		let proName = document.getElementById("proName") ? document.getElementById("proName").value : '';
		let proDiscount = document.getElementById("proDiscount") ? document.getElementById("proDiscount").value : '';
		let productStatus = document.querySelector('input[name="productStatus"]:checked').value; // 선택된 라디오 버튼 값
		const options = getOptionValues();

		console.log(options);

		if (petType === "" || proType === "" || proDetailType === "" || proName === "" || proDiscount === "" || options.some(opt => opt.price === "" || opt.count === "")) {
			alert('상품등록 필수 항목을 채워주세요.\n(펫타입, 상품종류, 상품타입, 상품명, 할인율, 옵션최소 1개)');
			return;
		}


		// FormData에 추가
		// 옵션 값을 가져와서 FormData에 추가
		// options 배열을 JSON 문자열로 변환하여 추가
		formData.append('options', JSON.stringify(options));

		formData.append('petType', petType);
		formData.append('proType', proType);
		formData.append('proDetailType', proDetailType);
		formData.append('filterType1', filterType1);
		formData.append('filterType2', filterType2);
		formData.append('proName', proName);
		formData.append('proDiscount', proDiscount);
		formData.append('productStatus', productStatus);

		// AJAX 요청
		$.ajax({
			url: '/admin/product/add', // 등록할 URL
			method: 'POST',
			data: formData,
			contentType: false, // contentType을 false로 설정
			processData: false, // processData를 false로 설정
			success: function(response) {
				// 성공적으로 등록된 경우 처리
				alert('제품이 등록되었습니다.');
				$('#productModal').hide();
			},
			error: function(xhr, status, error) {
				logger.error("상품 등록 중 오류 발생", error);
				console.error('Error registering product:', error);
				alert('제품 등록에 실패했습니다.');
			}
		});
























	});

	
	
	// 수정 버튼 이벤트 바인딩
			    $('#modify-btn').on('click', function() {
			        const proCode = $(this).data('product-code');
			        loadCouponForEdit(proCode); // 수정할 쿠폰 로드
					
					// 모달에 proCode를 저장하고 버튼 텍스트를 '수정'으로 설정
				    $('#registerCouponBtn').text('수정하기');
				    $('#productModal').data('proCode', proCode).show();
			    });



});