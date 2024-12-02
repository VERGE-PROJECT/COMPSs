# Copyright 2016 The Kubernetes Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# TAGS for docker built images
# IMPORTANT!!! Do not put '-' characters in tag use instead '_' to avoid problems  
PREFIX = registry.gitlab.bsc.es/ppc/software/compss/
COMPSS_VERSION = 3.3
MIN_BASE_TAG = min_base
FULL_BASE_TAG = full_base
COMPSS_TAG = compss_heuristics
ARCH_TAG=amd

# Check if the system architecture is arm
ifeq ($(shell uname -m),aarch64)
	ARCH_TAG=arm
endif

all: my_arch

amd:
	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/amd64 \
	--build-arg TARGETARCH=amd64 \
	-f utils/docker/base/Dockerfile \
	--target=base_all \
	-t $(PREFIX)$(MIN_BASE_TAG):$(COMPSS_VERSION)-amd .

	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/amd64 \
	--build-arg TARGETARCH=amd64 \
	-f utils/docker/base/Dockerfile \
	-t $(PREFIX)$(FULL_BASE_TAG):$(COMPSS_VERSION)-amd .

	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/amd64 \
	--build-arg FULL_BASE_CONTAINER=$(PREFIX)$(FULL_BASE_TAG):$(COMPSS_VERSION)-amd \
	--build-arg MIN_BASE_TAG=$(PREFIX)$(MIN_BASE_TAG):$(COMPSS_VERSION)-amd \
	-f Dockerfile \
	--target=ci \
	-t $(PREFIX)$(COMPSS_TAG):$(COMPSS_VERSION)-amd .

arm:
	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/arm64 \
	--build-arg TARGETARCH=arm64 \
	-f utils/docker/base/Dockerfile \
	--target=base_all \
	-t $(PREFIX)$(MIN_BASE_TAG):$(COMPSS_VERSION)-arm .

	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/arm64 \
	--build-arg TARGETARCH=arm64 \
	-f utils/docker/base/Dockerfile \
	-t $(PREFIX)$(FULL_BASE_TAG):$(COMPSS_VERSION)-arm .

	docker buildx build \
	--no-cache \
	--provenance false \
	--push \
	--platform linux/arm64 \
	--build-arg FULL_BASE_CONTAINER=$(PREFIX)$(FULL_BASE_TAG):$(COMPSS_VERSION)-arm \
	--build-arg MIN_BASE_CONTAINER=$(PREFIX)$(MIN_BASE_TAG):$(COMPSS_VERSION)-arm \
	-f Dockerfile \
	--target=compss \
	-t $(PREFIX)$(COMPSS_TAG):$(COMPSS_VERSION)-arm .

all_arch: 
	docker buildx create --name compss-framework --use

	$(MAKE) amd
	$(MAKE) arm

	$(call create_and_push_manifest,$(MIN_BASE_TAG))
	$(call create_and_push_manifest,$(FULL_BASE_TAG))
	$(call create_and_push_manifest,$(COMPSS_TAG))

	docker buildx rm compss-framework

define create_and_push_manifest
	docker manifest create $(PREFIX)$1:$(COMPSS_VERSION) \
		--amend $(PREFIX)$1:$(COMPSS_VERSION)-arm \
		--amend $(PREFIX)$1:$(COMPSS_VERSION)-amd
	docker manifest push --purge $(PREFIX)$1:$(COMPSS_VERSION)
endef

my_arch:
	docker buildx create --name compss-framework --use
	$(MAKE) $(ARCH_TAG)
	docker buildx rm compss-framework

clean:
	